package cz.cvut.fel.dsv.chat;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.51.0)",
    comments = "Source: chat.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NodeGrpc {

  private NodeGrpc() {}

  public static final String SERVICE_NAME = "Node";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getReceiveMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReceiveMessage",
      requestType = cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.class,
      responseType = cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getReceiveMessageMethod() {
    io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getReceiveMessageMethod;
    if ((getReceiveMessageMethod = NodeGrpc.getReceiveMessageMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getReceiveMessageMethod = NodeGrpc.getReceiveMessageMethod) == null) {
          NodeGrpc.getReceiveMessageMethod = getReceiveMessageMethod =
              io.grpc.MethodDescriptor.<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReceiveMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("ReceiveMessage"))
              .build();
        }
      }
    }
    return getReceiveMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRegisterUserNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterUserNode",
      requestType = cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm.class,
      responseType = cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRegisterUserNodeMethod() {
    io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRegisterUserNodeMethod;
    if ((getRegisterUserNodeMethod = NodeGrpc.getRegisterUserNodeMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getRegisterUserNodeMethod = NodeGrpc.getRegisterUserNodeMethod) == null) {
          NodeGrpc.getRegisterUserNodeMethod = getRegisterUserNodeMethod =
              io.grpc.MethodDescriptor.<cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterUserNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("RegisterUserNode"))
              .build();
        }
      }
    }
    return getRegisterUserNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRouteMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RouteMessage",
      requestType = cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.class,
      responseType = cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRouteMessageMethod() {
    io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getRouteMessageMethod;
    if ((getRouteMessageMethod = NodeGrpc.getRouteMessageMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getRouteMessageMethod = NodeGrpc.getRouteMessageMethod) == null) {
          NodeGrpc.getRouteMessageMethod = getRouteMessageMethod =
              io.grpc.MethodDescriptor.<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RouteMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("RouteMessage"))
              .build();
        }
      }
    }
    return getRouteMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getSendHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendHello",
      requestType = cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.class,
      responseType = cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
      cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getSendHelloMethod() {
    io.grpc.MethodDescriptor<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> getSendHelloMethod;
    if ((getSendHelloMethod = NodeGrpc.getSendHelloMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSendHelloMethod = NodeGrpc.getSendHelloMethod) == null) {
          NodeGrpc.getSendHelloMethod = getSendHelloMethod =
              io.grpc.MethodDescriptor.<cz.cvut.fel.dsv.chat.ChatProto.ChatRequest, cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cz.cvut.fel.dsv.chat.ChatProto.ChatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("SendHello"))
              .build();
        }
      }
    }
    return getSendHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeStub>() {
        @java.lang.Override
        public NodeStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeStub(channel, callOptions);
        }
      };
    return NodeStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub>() {
        @java.lang.Override
        public NodeBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeBlockingStub(channel, callOptions);
        }
      };
    return NodeBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub>() {
        @java.lang.Override
        public NodeFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeFutureStub(channel, callOptions);
        }
      };
    return NodeFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NodeImplBase implements io.grpc.BindableService {

    /**
     */
    public void receiveMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReceiveMessageMethod(), responseObserver);
    }

    /**
     */
    public void registerUserNode(cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterUserNodeMethod(), responseObserver);
    }

    /**
     */
    public void routeMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRouteMessageMethod(), responseObserver);
    }

    /**
     */
    public void sendHello(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReceiveMessageMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
                cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>(
                  this, METHODID_RECEIVE_MESSAGE)))
          .addMethod(
            getRegisterUserNodeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm,
                cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>(
                  this, METHODID_REGISTER_USER_NODE)))
          .addMethod(
            getRouteMessageMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
                cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>(
                  this, METHODID_ROUTE_MESSAGE)))
          .addMethod(
            getSendHelloMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                cz.cvut.fel.dsv.chat.ChatProto.ChatRequest,
                cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>(
                  this, METHODID_SEND_HELLO)))
          .build();
    }
  }

  /**
   */
  public static final class NodeStub extends io.grpc.stub.AbstractAsyncStub<NodeStub> {
    private NodeStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeStub(channel, callOptions);
    }

    /**
     */
    public void receiveMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReceiveMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerUserNode(cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterUserNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void routeMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRouteMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendHello(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request,
        io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeBlockingStub extends io.grpc.stub.AbstractBlockingStub<NodeBlockingStub> {
    private NodeBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeBlockingStub(channel, callOptions);
    }

    /**
     */
    public cz.cvut.fel.dsv.chat.ChatProto.ChatResponse receiveMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReceiveMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public cz.cvut.fel.dsv.chat.ChatProto.ChatResponse registerUserNode(cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterUserNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public cz.cvut.fel.dsv.chat.ChatProto.ChatResponse routeMessage(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRouteMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public cz.cvut.fel.dsv.chat.ChatProto.ChatResponse sendHello(cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeFutureStub extends io.grpc.stub.AbstractFutureStub<NodeFutureStub> {
    private NodeFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> receiveMessage(
        cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReceiveMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> registerUserNode(
        cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterUserNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> routeMessage(
        cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRouteMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse> sendHello(
        cz.cvut.fel.dsv.chat.ChatProto.ChatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECEIVE_MESSAGE = 0;
  private static final int METHODID_REGISTER_USER_NODE = 1;
  private static final int METHODID_ROUTE_MESSAGE = 2;
  private static final int METHODID_SEND_HELLO = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECEIVE_MESSAGE:
          serviceImpl.receiveMessage((cz.cvut.fel.dsv.chat.ChatProto.ChatRequest) request,
              (io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>) responseObserver);
          break;
        case METHODID_REGISTER_USER_NODE:
          serviceImpl.registerUserNode((cz.cvut.fel.dsv.chat.ChatProto.RegistrationForm) request,
              (io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>) responseObserver);
          break;
        case METHODID_ROUTE_MESSAGE:
          serviceImpl.routeMessage((cz.cvut.fel.dsv.chat.ChatProto.ChatRequest) request,
              (io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>) responseObserver);
          break;
        case METHODID_SEND_HELLO:
          serviceImpl.sendHello((cz.cvut.fel.dsv.chat.ChatProto.ChatRequest) request,
              (io.grpc.stub.StreamObserver<cz.cvut.fel.dsv.chat.ChatProto.ChatResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cz.cvut.fel.dsv.chat.ChatProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Node");
    }
  }

  private static final class NodeFileDescriptorSupplier
      extends NodeBaseDescriptorSupplier {
    NodeFileDescriptorSupplier() {}
  }

  private static final class NodeMethodDescriptorSupplier
      extends NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NodeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeFileDescriptorSupplier())
              .addMethod(getReceiveMessageMethod())
              .addMethod(getRegisterUserNodeMethod())
              .addMethod(getRouteMessageMethod())
              .addMethod(getSendHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
