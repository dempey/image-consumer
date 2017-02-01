# image-consumer

rough steps to install on aws
1. create s3 bucket and change the AmazonConfiguration.BUCKET to the name of your bucket
1. create a RDS MySql Instance with the dbInstanceName, username and password the same as the ones in AmazonConfiguration's @EnableRdsInstance (or change them to ones that you created for the database instance)
1. get the endpoint for the new instance and open it in a sql client (I used MySql Workbench) and create the schema "mydb" and set it to default
1. run the database scripts in the src/main/resources/db against that schema
1. go into the security to add an inbound rule
1. put the access and secret keys into the application.properties file
1. mvn install
1. deploy the jar to the Elastic Beanstalk