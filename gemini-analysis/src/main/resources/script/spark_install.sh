groupadd spark
useradd -d /aifs01/users/devspark -g spark -m devspark

passwd devspark
1qaz@WSX

ssh-keygen -b 2048 -t rsa
scp -P 22 /aifs01/users/devspark/.ssh/id_rsa.pub devspark@10.1.241.18:/aifs01/users/devspark/.ssh/id_rsa_18.pub
scp -P 22 /aifs01/users/devspark/.ssh/id_rsa.pub devspark@10.1.241.19:/aifs01/users/devspark/.ssh/id_rsa_18.pub
scp -P 22 /aifs01/users/devspark/.ssh/id_rsa.pub devspark@10.1.241.20:/aifs01/users/devspark/.ssh/id_rsa_18.pub
cat id_rsa_18.pub >> authorized_keys

wget http://d3kbcqa49mib13.cloudfront.net/spark-2.0.2-bin-hadoop2.7.tgz

scp -r -P 22022 /aifs01/users/devspk01/receiver devspk01@10.19.7.67:/aifs01/users/devspk01/receiver
scp -r -P 22022 /aifs01/users/devspk01/spark-2.0.1-bin-hadoop2.7 devspk01@10.19.7.67:/aifs01/users/devspk01/spark-2.0.1-bin-hadoop2.7

tar -xzf spark-2.0.2-bin-hadoop2.7.tgz

nohup ./spark/bin/spark-submit --class "com.a.eye.gemini.analysis.AnalysisStartUp" --master spark://master:17910 --supervise --total-executor-cores 1 /aifs01/users/devspark/analysis/gemini-analysis-1.0.0-alpha1.jar > run.log 2>&1 &