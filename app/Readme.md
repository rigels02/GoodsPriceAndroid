# GoodsPrice Android application

For data storage the GoodsPrice file storage controller is used like in desktop application:
goods-goodsprice.
The file storage controller is defined in class CoreLayer.

## Features
- the application is able to send/read data via Rest API exposed by server side 
desktop application: goods-GoodsPriceSpring (Spring Boot application).
- the application is able to import/export data in csv format (by Sharing)

There is activity called TCPClientActivity for TCP data transfer what was not
completed yet, but the Rest API is best solution.

## Data Sharing
- import CSV data by Android Sharing to activity: ImportGoodsCSV
- export CSV data by Sharing via menu 'Share as CSV'

## Rest API
- to access Rest Api resources the spring-android-rest-template is used

~~~
implementation 'org.springframework.android:spring-android-rest-template:2.0.0.M3'
~~~  

- for Json data binding jackson-databind used:

~~~
implementation 'com.fasterxml.jackson.core:jackson-databind:2.3.2'
~~~

URL to server is in format: 
~~~
http://<ip>:8080/goods
~~~
Where &lt;ip> is server's host ip address.
