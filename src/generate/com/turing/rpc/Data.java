// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Data.proto

package com.turing.rpc;

public final class Data {
  private Data() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Log_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Log_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Log_ExtendInfoEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Log_ExtendInfoEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GetRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GetRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GetRequest_ExtendInfoEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GetRequest_ExtendInfoEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nData.proto\"\255\001\n\003Log\022\r\n\005group\030\001 \001(\t\022\013\n\003k" +
      "ey\030\002 \001(\t\022\014\n\004data\030\003 \001(\014\022\014\n\004type\030\004 \001(\t\022\021\n\t" +
      "operation\030\005 \001(\t\022(\n\nextendInfo\030\006 \003(\0132\024.Lo" +
      "g.ExtendInfoEntry\0321\n\017ExtendInfoEntry\022\013\n\003" +
      "key\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:\0028\001\"\215\001\n\nGetRequ" +
      "est\022\r\n\005group\030\001 \001(\t\022\014\n\004data\030\002 \001(\014\022/\n\nexte" +
      "ndInfo\030\003 \003(\0132\033.GetRequest.ExtendInfoEntr" +
      "y\0321\n\017ExtendInfoEntry\022\013\n\003key\030\001 \001(\t\022\r\n\005val" +
      "ue\030\002 \001(\t:\0028\001B\022\n\016com.turing.rpcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_Log_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Log_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Log_descriptor,
        new java.lang.String[] { "Group", "Key", "Data", "Type", "Operation", "ExtendInfo", });
    internal_static_Log_ExtendInfoEntry_descriptor =
      internal_static_Log_descriptor.getNestedTypes().get(0);
    internal_static_Log_ExtendInfoEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Log_ExtendInfoEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_GetRequest_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_GetRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GetRequest_descriptor,
        new java.lang.String[] { "Group", "Data", "ExtendInfo", });
    internal_static_GetRequest_ExtendInfoEntry_descriptor =
      internal_static_GetRequest_descriptor.getNestedTypes().get(0);
    internal_static_GetRequest_ExtendInfoEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GetRequest_ExtendInfoEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}