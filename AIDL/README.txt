AIDL:
Android Interface Definition Language
Android 接口定义语言，用于进程间通信

aidl文件路径 main->aidl->code package

服务端
1.定义AIDL接口-IBookManager.aidl
    作用：通信接口，用来连接客户端和服务端的接口。

2.定义JavaBean-Book.java
    作用：用来传递数据，需要实现Parcelable接口

3.定义JavaBean接口-Book.aidl
    实体类的映射 aidl 文件，文件目录结构必须和java代码中的Book.java目录结构完全一致
    除基本类型数据和list、map外，其他自定义的类型都需要有aidl声明,并且

4.编译
    由编译器生成java代码

5.定义service-BookManageService.java
    作用：提供给客户端binding
    onBind方法返回 编译 时自动生成的IBookManager.aidl中的Binder,一般时ClassName.Stub();
    AndroidManifest.xml声明时还需要加intent-filter，客户端binding的时候需要用隐式意图

客户端
1.复制 IBookManager.aidl Book.java Book.aidl，包名结构和服务端完全一致
2.编译生成java代码
3.bindingService，通过隐式意图binding服务的service，通过iBookManger = IBookManger.Stub.asInterface(service);完成对服务端
的操作