# skaffold-sbt-example

## Prerequisites

* [AdoptOpenJDK](https://adoptopenjdk.net/)   
* [sbt](https://www.scala-sbt.org/)
* [Minikube](https://minikube.sigs.k8s.io/docs/)
* [Kubernetes](https://kubernetes.io/docs/)
* [Skaffold](https://skaffold.dev/docs/)


## Getting Started

```shell
# Prepare to use Kubernetes
minikube start && minikube tunnel

# Deploy this application to Kubernetes
skaffold dev

# Send a request to the deployed application
curl http://127.0.0.1:8080/hello
```


## Environment Variables

* `AKKA_CLUSTER_NODE_HOSTNAME (string, default="127.0.0.1")`  
  Akka Cluster Node Hostname
* `APP_HTTP_HOSTNAME (string, default="127.0.0.1")`  
  HTTP Application Server Hostname
* `APP_MANAGEMENT_HOSTNAME (string, default="127.0.0.1")`  
  HTTP Management Server Hostname
