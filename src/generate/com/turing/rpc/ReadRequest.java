// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: consistency.proto

package com.turing.rpc;

/**
 * Protobuf type {@code ReadRequest}
 */
public  final class ReadRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:ReadRequest)
    ReadRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ReadRequest.newBuilder() to construct.
  private ReadRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ReadRequest() {
    group_ = "";
    data_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ReadRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ReadRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            group_ = s;
            break;
          }
          case 18: {

            data_ = input.readBytes();
            break;
          }
          case 26: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              extendInfo_ = com.google.protobuf.MapField.newMapField(
                  ExtendInfoDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000001;
            }
            com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
            extendInfo__ = input.readMessage(
                ExtendInfoDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            extendInfo_.getMutableMap().put(
                extendInfo__.getKey(), extendInfo__.getValue());
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.turing.rpc.Consistency.internal_static_ReadRequest_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 3:
        return internalGetExtendInfo();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.turing.rpc.Consistency.internal_static_ReadRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.turing.rpc.ReadRequest.class, com.turing.rpc.ReadRequest.Builder.class);
  }

  public static final int GROUP_FIELD_NUMBER = 1;
  private volatile java.lang.Object group_;
  /**
   * <code>string group = 1;</code>
   */
  public java.lang.String getGroup() {
    java.lang.Object ref = group_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      group_ = s;
      return s;
    }
  }
  /**
   * <code>string group = 1;</code>
   */
  public com.google.protobuf.ByteString
      getGroupBytes() {
    java.lang.Object ref = group_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      group_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int DATA_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString data_;
  /**
   * <code>bytes data = 2;</code>
   */
  public com.google.protobuf.ByteString getData() {
    return data_;
  }

  public static final int EXTENDINFO_FIELD_NUMBER = 3;
  private static final class ExtendInfoDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, java.lang.String> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, java.lang.String>newDefaultInstance(
                com.turing.rpc.Consistency.internal_static_ReadRequest_ExtendInfoEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  private com.google.protobuf.MapField<
      java.lang.String, java.lang.String> extendInfo_;
  private com.google.protobuf.MapField<java.lang.String, java.lang.String>
  internalGetExtendInfo() {
    if (extendInfo_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          ExtendInfoDefaultEntryHolder.defaultEntry);
    }
    return extendInfo_;
  }

  public int getExtendInfoCount() {
    return internalGetExtendInfo().getMap().size();
  }
  /**
   * <code>map&lt;string, string&gt; extendInfo = 3;</code>
   */

  public boolean containsExtendInfo(
      java.lang.String key) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    return internalGetExtendInfo().getMap().containsKey(key);
  }
  /**
   * Use {@link #getExtendInfoMap()} instead.
   */
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.String> getExtendInfo() {
    return getExtendInfoMap();
  }
  /**
   * <code>map&lt;string, string&gt; extendInfo = 3;</code>
   */

  public java.util.Map<java.lang.String, java.lang.String> getExtendInfoMap() {
    return internalGetExtendInfo().getMap();
  }
  /**
   * <code>map&lt;string, string&gt; extendInfo = 3;</code>
   */

  public java.lang.String getExtendInfoOrDefault(
      java.lang.String key,
      java.lang.String defaultValue) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetExtendInfo().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;string, string&gt; extendInfo = 3;</code>
   */

  public java.lang.String getExtendInfoOrThrow(
      java.lang.String key) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetExtendInfo().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getGroupBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, group_);
    }
    if (!data_.isEmpty()) {
      output.writeBytes(2, data_);
    }
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetExtendInfo(),
        ExtendInfoDefaultEntryHolder.defaultEntry,
        3);
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getGroupBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, group_);
    }
    if (!data_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(2, data_);
    }
    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry
         : internalGetExtendInfo().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
      extendInfo__ = ExtendInfoDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, extendInfo__);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.turing.rpc.ReadRequest)) {
      return super.equals(obj);
    }
    com.turing.rpc.ReadRequest other = (com.turing.rpc.ReadRequest) obj;

    if (!getGroup()
        .equals(other.getGroup())) return false;
    if (!getData()
        .equals(other.getData())) return false;
    if (!internalGetExtendInfo().equals(
        other.internalGetExtendInfo())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + GROUP_FIELD_NUMBER;
    hash = (53 * hash) + getGroup().hashCode();
    hash = (37 * hash) + DATA_FIELD_NUMBER;
    hash = (53 * hash) + getData().hashCode();
    if (!internalGetExtendInfo().getMap().isEmpty()) {
      hash = (37 * hash) + EXTENDINFO_FIELD_NUMBER;
      hash = (53 * hash) + internalGetExtendInfo().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.turing.rpc.ReadRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.turing.rpc.ReadRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.turing.rpc.ReadRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.turing.rpc.ReadRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.turing.rpc.ReadRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.turing.rpc.ReadRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.turing.rpc.ReadRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code ReadRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:ReadRequest)
      com.turing.rpc.ReadRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.turing.rpc.Consistency.internal_static_ReadRequest_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 3:
          return internalGetExtendInfo();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 3:
          return internalGetMutableExtendInfo();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.turing.rpc.Consistency.internal_static_ReadRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.turing.rpc.ReadRequest.class, com.turing.rpc.ReadRequest.Builder.class);
    }

    // Construct using com.turing.rpc.ReadRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      group_ = "";

      data_ = com.google.protobuf.ByteString.EMPTY;

      internalGetMutableExtendInfo().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.turing.rpc.Consistency.internal_static_ReadRequest_descriptor;
    }

    @java.lang.Override
    public com.turing.rpc.ReadRequest getDefaultInstanceForType() {
      return com.turing.rpc.ReadRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.turing.rpc.ReadRequest build() {
      com.turing.rpc.ReadRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.turing.rpc.ReadRequest buildPartial() {
      com.turing.rpc.ReadRequest result = new com.turing.rpc.ReadRequest(this);
      int from_bitField0_ = bitField0_;
      result.group_ = group_;
      result.data_ = data_;
      result.extendInfo_ = internalGetExtendInfo();
      result.extendInfo_.makeImmutable();
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.turing.rpc.ReadRequest) {
        return mergeFrom((com.turing.rpc.ReadRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.turing.rpc.ReadRequest other) {
      if (other == com.turing.rpc.ReadRequest.getDefaultInstance()) return this;
      if (!other.getGroup().isEmpty()) {
        group_ = other.group_;
        onChanged();
      }
      if (other.getData() != com.google.protobuf.ByteString.EMPTY) {
        setData(other.getData());
      }
      internalGetMutableExtendInfo().mergeFrom(
          other.internalGetExtendInfo());
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.turing.rpc.ReadRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.turing.rpc.ReadRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object group_ = "";
    /**
     * <code>string group = 1;</code>
     */
    public java.lang.String getGroup() {
      java.lang.Object ref = group_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        group_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string group = 1;</code>
     */
    public com.google.protobuf.ByteString
        getGroupBytes() {
      java.lang.Object ref = group_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        group_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string group = 1;</code>
     */
    public Builder setGroup(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      group_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string group = 1;</code>
     */
    public Builder clearGroup() {
      
      group_ = getDefaultInstance().getGroup();
      onChanged();
      return this;
    }
    /**
     * <code>string group = 1;</code>
     */
    public Builder setGroupBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      group_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes data = 2;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }
    /**
     * <code>bytes data = 2;</code>
     */
    public Builder setData(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      data_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes data = 2;</code>
     */
    public Builder clearData() {
      
      data_ = getDefaultInstance().getData();
      onChanged();
      return this;
    }

    private com.google.protobuf.MapField<
        java.lang.String, java.lang.String> extendInfo_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetExtendInfo() {
      if (extendInfo_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            ExtendInfoDefaultEntryHolder.defaultEntry);
      }
      return extendInfo_;
    }
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetMutableExtendInfo() {
      onChanged();;
      if (extendInfo_ == null) {
        extendInfo_ = com.google.protobuf.MapField.newMapField(
            ExtendInfoDefaultEntryHolder.defaultEntry);
      }
      if (!extendInfo_.isMutable()) {
        extendInfo_ = extendInfo_.copy();
      }
      return extendInfo_;
    }

    public int getExtendInfoCount() {
      return internalGetExtendInfo().getMap().size();
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public boolean containsExtendInfo(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      return internalGetExtendInfo().getMap().containsKey(key);
    }
    /**
     * Use {@link #getExtendInfoMap()} instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getExtendInfo() {
      return getExtendInfoMap();
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public java.util.Map<java.lang.String, java.lang.String> getExtendInfoMap() {
      return internalGetExtendInfo().getMap();
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public java.lang.String getExtendInfoOrDefault(
        java.lang.String key,
        java.lang.String defaultValue) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetExtendInfo().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public java.lang.String getExtendInfoOrThrow(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetExtendInfo().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearExtendInfo() {
      internalGetMutableExtendInfo().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public Builder removeExtendInfo(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      internalGetMutableExtendInfo().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String>
    getMutableExtendInfo() {
      return internalGetMutableExtendInfo().getMutableMap();
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */
    public Builder putExtendInfo(
        java.lang.String key,
        java.lang.String value) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      if (value == null) { throw new java.lang.NullPointerException(); }
      internalGetMutableExtendInfo().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; extendInfo = 3;</code>
     */

    public Builder putAllExtendInfo(
        java.util.Map<java.lang.String, java.lang.String> values) {
      internalGetMutableExtendInfo().getMutableMap()
          .putAll(values);
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:ReadRequest)
  }

  // @@protoc_insertion_point(class_scope:ReadRequest)
  private static final com.turing.rpc.ReadRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.turing.rpc.ReadRequest();
  }

  public static com.turing.rpc.ReadRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ReadRequest>
      PARSER = new com.google.protobuf.AbstractParser<ReadRequest>() {
    @java.lang.Override
    public ReadRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ReadRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ReadRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ReadRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.turing.rpc.ReadRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

