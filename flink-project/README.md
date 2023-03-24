# Flink Demo for evaluation purposes

Step by step guide towards a running flink job on minikube

# Prerequisites.

If you want to run Kubernetes locally, we recommend using MiniKube.

    # homebrew install minkube
    # homebrew install apache-flink

    # minkube start

If using MiniKube please make sure to execute minikube 

    $ ssh 'sudo ip link set docker0 promisc on' 

before deploying a Flink cluster. Otherwise Flink components are not able to reference themselves through a Kubernetes service.

# Starting a Kubernetes Cluster

A Flink Session cluster is executed as a long-running Kubernetes Deployment. You can run multiple Flink jobs on a Session cluster. Each job needs to be submitted to the cluster after the cluster has been deployed.

A Flink Session cluster deployment in Kubernetes has at least three components:

a Deployment which runs a JobManager
a Deployment for a pool of TaskManagers
a Service exposing the JobManager’s REST and UI ports

Using the file contents provided in the the common resource definitions, create the following files provided in src/main/charts and create the respective components with the kubectl command:



    # Configuration and service definition
    $ kubectl create -f flink-configuration-configmap.yaml
    $ kubectl create -f jobmanager-service.yaml
    # Create the deployments for the cluster
    $ kubectl create -f jobmanager-session-deployment-non-ha.yaml
    $ kubectl create -f taskmanager-session-deployment.yaml


Next, we set up a port forward to access the Flink UI and submit jobs:

Run 

    $ kubectl port-forward ${flink-jobmanager-pod} 8081:8081 

to forward your jobmanager’s web ui port to local 8081.
Navigate to http://localhost:8081 in your browser.
Moreover, you could use the following command below to submit jobs to the cluster:

    $ ./bin/flink run -m localhost:8081 ./examples/streaming/TopSpeedWindowing.jar

You can tear down the cluster using the following commands:

    $ kubectl delete -f jobmanager-service.yaml
    $ kubectl delete -f flink-configuration-configmap.yaml
    $ kubectl delete -f taskmanager-session-deployment.yaml
    $ kubectl delete -f jobmanager-session-deployment-non-ha.yaml

# To do a simple run.

Just execute locally.

# Run the jar application.

Package the StreamingCars Application as a .jar file

    $ mvn clean package

Submit the .jar file to the jobmanager

    $ flink run -m localhost:8081 $OATH_TO_YOUR_JAR_FILE


# Needs to be done

- package .jar file in a docker container, deploy to the cluster and run from there
- scheduling for batch processes could be an issue. Check the following sources: 
  - https://flink.apache.org/2020/12/02/improvements-in-task-scheduling-for-batch-workloads-in-apache-flink-1.12/#scheduling-strate[…]n-flink-before-112
  - https://nightlies.apache.org/flink/flink-docs-release-1.17/docs/deployment/elastic_scaling/#adaptive-batch-scheduler