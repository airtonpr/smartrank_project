<h1>***** SmartRank ***** </h1>
<br>

Resource scarcity is a major obstacle for many mobile applications, since devices have limited energy power and processing
potential. As an example, there are applications that seamlessly augment human cognition and typically require resources that
far outstrip mobile hardware's capabilities, such as language translation, speech recognition, and face recognition. A new trend
has been explored to tackle this problem, the use of cloud computing. This study presents SmartRank, a scheduling framework to
perform load partitioning and offloading for mobile applications using cloud computing to increase performance in terms of response
time. 
<br>

Projects 

<p>
<ul>
<li><b>SmartRank-Client</b>: A simple Android client application that send pictures for a server to perform face recognition.
The main class is SmartRank-Client\src\com\main\MainImageActivity.java</li>

<li><b>SmartRank-Cloudlet-Manager</b>: A cloudlet is a resource-rich computer or cluster of computers with fast Internet and available for use by nearby mobile
devices. In our case is a simple server that receive the requests and schedule for a bunch of other servers.
The main class is SmartRank-Cloudlet-Manager\src\main\CloudletTransmitter.java</li>

<li><b>SmartRank-Server</b>: It includes the software responsible for performing the face recognition process. There is also an antivirus software in it.
The main class is SmartRank-Server\src\main\TCPServerManager.java</li>
</ul>

<br>
The code is configured to run at localhost, both Cloudlet and Server. You must only to install the message server that
interconnect both parts: RabbitMq Server: http://www.rabbitmq.com/download.html 
<br>
In case you want to change the servers location just reconfigure the properties files: 
Cloudlet\config-cloudlet.properties e SmartRank-Server\config-server.properties
<br>
The other projects are secondary. They were used in other experiments and may be useful to other researchers in MCC.
<br>
<ul>
<li><b>FaceRecognitionAndroid</b>: Runs a face recognition on android device standalone</li>
<li><b>VirusScanningAndroid</b>: Runs a antivirus scanning on android device standalone</li>
<li><b>ParserPowerTutor</b>: PowerTutor is an app used to capture mobile energy consumption (http://powertutor.org/). This java project has a simple parser to treat the log generated by PowerTutor.</li>
</ul>


</p>