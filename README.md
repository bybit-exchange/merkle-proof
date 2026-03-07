# Merkle Proof Validation Tool

##  How to Proceed
###  1.Build the executable file
Download the latest source code files, and ensure that  [JDK](https://openjdk.org/) (version 1.8 and above) and [3697503](https://maven.apache.org/) have been installed on your workstation or laptop.
+ Resolve dependencies, compile and package
```java
    mvn clean package
```
+ check the compiled jar package, as follows, in the current project directory.
```java
    ./target/BybitMerkleProofValidator.jar
```
###  2.Perform Your Verification
+ You can paste the proof file copied from the Bybit website (https://www.bybit.com) into a new file in the target current directory, and name the file as myProof.json. Then, execute the following command:
```java
    java -jar target/BybitMerkleProofValidator.jar myProof.json
```
+ Next, you can run the verified program and observe its output data.
```java
    1 ......
    2 validate result is true/false
```