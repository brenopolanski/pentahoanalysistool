# Pendahuluan #

Aplikasi PAT adalah penggabungan dari beberapa layer dan framework yang berbeda-beda ke dalam satu project. Halaman antar muka (_front end_) akan dihasilkan oleh framework [GWT](http://code.google.com/webtoolkit/) (Google Web Toolkit. Untuk back-endnya menggunakan framework Spring. Titik koneksi antar dua framework ini adalah paket (package) org.pentaho.pat.rpc. Paket ini mendefinisikan semua _interface_ yang akan digunakan oleh GUI (_Graphical User Interface_).


Kerangka utama aplikasi ini adalah sebagai berikut
  * org.pentaho.pat : Paket utama (_main package_).
    * org.pentaho.pat.client : Segala sesuatu yang berhubungan dengan GUI.
    * org.pentaho.pat.rpc : Interface RPC (_Remote Procedure Call_) untuk komunikasi client-server.
    * org.pentaho.pat.server : Package utama segala hal yang berhubungan dengan server.
      * org.pentaho.pat.server.data : This is the persistence classes.
      * org.pentaho.pat.server.services : This is the service layer of the server.
      * org.pentaho.pat.server.servlet : This is the web layer of the server. It contains all the servlets that implement the RPC interfaces.

# Ketergantungan Proyek (_Project Dependencies_) #

Proyek ini bergantung ke beberapa proyek berikut :

  * [Spring framework](http://www.springsource.org).
  * [Google web toolkit](http://code.google.com/webtoolkit/).
  * [www.acegisecurity.org ACEGI security framework].

# Anatomi dari suatu _server request_ #

This section describes how requests with the backend system are handled through the layers of the application.

## First contact : RPC interfaces ##

The only layer that a client needs to be aware of is the RPC interfaces defined in org.pentaho.pat.rpc. Any GUI implementation MUST conform to it. Those interfaces are in turn implemented as web servlets. We could have implemented them with any other technology we needed.

The RPC implementations responsibilities are:

  * Expose the services to the external world.
  * Authenticate clients.
  * Allows a single user to work with more than one "window session" simult.
  * Prevent clients from spoofing a window session id using the specific implementation's authentications technologies.

## Service layer ##

At first sight, this might look like a duplication of the RPC implementations, but there is something fundamentally different done in this layer.

First off, those classes are not bound to any particular technology other than Olap4j. It is completely abstracted from the communication channels that feed it. It is neither a servlet or a GWT module. It is a plain old Java object implementing a given service interface.

This ensures that the whole business logic can be wrapped with whatever server communication technology.

It's responsibilities are as follows :

  * Supposes that the window session id sent (guid) is valid and has not been spoofed, it will not perform any validation on it.
  * Translates the service methods parameters into operations performed on Olap4j specifically.

The service layer is thus the core of PAT. PAT was meant to be this way so it keeps the overhead at a minimum and relays all query operations to the Olap4j model layer. This layer is the last invoked in a request.

## Persistance helpers ##

PAT is a stateful session system. It stores data internally concerning the user sessions. It is thus required that users create sessions and destroy them upon completion of their tasks.

The data that is stored varies in it's nature. It could be :

  * Variables concerning the current state of the GUI controls; like which cube is currently selected for example.
  * The connection objects created by window sessions.
  * Olap4j query objects and result tables.