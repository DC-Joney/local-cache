package com.dc.cache.raft.discover;

import com.google.common.collect.Iterables;
import com.turing.common.annotaion.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.bind.*;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.function.SingletonSupplier;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * 通过Spring 的Binder 工具类来加载对应的配置文件
 *
 * @see ConfigurationPropertySources
 * @see BindHandler
 * @see Bindable
 * @see Binder
 * @see org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar
 * @see org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor
 */
@Slf4j
@Component
public class SpringResourceLoader implements ResourceLoaderAware, InitializingBean,
        ApplicationContextAware, EnvironmentAware {


    private static final SingletonSupplier<PropertySourceLoader> yamlSourceLoader =
            SingletonSupplier.of(YamlPropertySourceLoader::new);

    private static final SingletonSupplier<PropertySourceLoader> propertiesPropertySourceLoader =
            SingletonSupplier.of(PropertiesPropertySourceLoader::new);

    private static final String DEFAULT_CLASSPATH_PREFIX = "classpath:";

    private final Set<PropertySourceLoader> loaderCache = new HashSet<>();

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    private ConfigurableEnvironment environment;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        List<PropertySourceLoader> sourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, ClassUtils.getDefaultClassLoader());
        loaderCache.addAll(sourceLoaders);
        //如果没有加载到SourceLoader 则添加默认的sourceLoader
        if (loaderCache.isEmpty()) {
            loaderCache.add(yamlSourceLoader.get());
            loaderCache.add(propertiesPropertySourceLoader.get());
        }
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void setEnvironment(@NonNull Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment, "The environment must be instance of ConfigurableEnvironment");
        this.environment = (ConfigurableEnvironment) environment;
    }


    public <T> BindResult<T> bindSpringProperty(Class<T> propertyClass, String prefix) {
        return Binder.get(environment).bind(prefix, propertyClass);
    }


    public <T> BindResult<T> bindSpringProperty(T instance, String prefix) {
        return Binder.get(environment)
                .bind(prefix, Bindable.ofInstance(instance));
    }


    /**
     * @param resourceLocation 绑定instance 对应的resource
     * @param instance         被绑定的实例
     * @param prefix           被绑定的前缀
     */
    public <T> BindResult<T> bindProperty(String resourceLocation, String prefix, Supplier<T> instance) {
        try {
            if (!resourceLocation.startsWith(DEFAULT_CLASSPATH_PREFIX)) {
                resourceLocation = DEFAULT_CLASSPATH_PREFIX + "/" + resourceLocation;
            }

            //通过resourceLoader加载资源配置
            Resource resource = resourceLoader.getResource(resourceLocation);
            Optional<PropertySourceLoader> loader;

            //如果文件存在，并且也存在resource 对应的loader
            if (resource.exists() && (loader = findLoader(resourceLocation)).isPresent()) {
                List<PropertySource<?>> loaderSource = loader.get().load(resourceLocation, resource);
                if (!loaderSource.isEmpty()) {
                    Binder binder = new Binder(getConfigurationPropertySources(loaderSource),
                            getPropertySourcesPlaceholdersResolver(loaderSource), getConversionService(),
                            getPropertyEditorInitializer());

                    BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();
                    UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
                    BindHandler noUnboundElementsBindHandler = new NoUnboundElementsBindHandler(handler, filter);
                    T bindInstance = Objects.requireNonNull(instance.get(), "Instance supplied must be non-null");
                    Bindable<T> bindable = Bindable.ofInstance(bindInstance);
                    return binder.bind(prefix, bindable, noUnboundElementsBindHandler);
                }
            }


        } catch (Exception e) {
            log.error("Bind resources error: " + e.getMessage(), e);
        }

        // if resource not bind successfully then fallback to the environment
        T bindInstance = Objects.requireNonNull(instance.get(), "Instance supplied must be non-null");
        Bindable<T> bindable = Bindable.ofInstance(bindInstance);
        BindHandler bindHandler = new IgnoreErrorsBindHandler();
        return Binder.get(environment).bind(prefix, bindable, bindHandler);
    }


    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources(Iterable<PropertySource<?>> propertySources) {
        Iterable<PropertySource<?>> newPropertySources = Iterables.concat(propertySources, environment.getPropertySources());
        return ConfigurationPropertySources.from(newPropertySources);
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> propertySources) {
        return new PropertySourcesPlaceholdersResolver(propertySources);
    }

    private ConversionService getConversionService() {
        return ApplicationConversionService.getSharedInstance();
    }

    private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.applicationContext)
                    .getBeanFactory()::copyRegisteredEditorsTo;
        }
        return null;
    }


    private Optional<PropertySourceLoader> findLoader(String resourceName) {
        return loaderCache.stream()
                .flatMap(loader -> Arrays.stream(loader.getFileExtensions())
                        .map(ext -> Pair.of(ext, loader))
                )
                .filter(pair -> StringUtils.endsWithIgnoreCase(resourceName, pair.getLeft()))
                .findFirst()
                .map(Pair::getRight);
    }

}
