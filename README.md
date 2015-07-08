# bigdata_monitoramento_de_redes_sociais
Trabalho Prático

## Setup IntelliJ IDEA
Clone repository:
```sh
$ git clone https://github.com/brunorafaeli/bigdata_monitoramento_de_redes_sociais.git
```
In IntelliJ IDEA:
- File -> New -> Project from Exsiting Sources...
- Select social-networks-monitor-alarm-idea-project directory (ok)
- Select Import project from external model -> sbt (next)
- Check:
  - Create directories for empty content root automatically
  - Download source and docs
  - Download SBT source and docs
- Select Project sdk = 1.8
- Click Finish

## Run

spark-submit --master local[4] social-networks-monitor-alarm-idea-project.jar consumerKey consumerSecret accessToken accessTokenSecret dictfilenm outdir

##Generate Dataset
<p><b>Command to run:</b> python generate_dataset.py</p>
  <p>Line 12 - Decide the number of dataset lines</p>
  <p>Line 13 - Decide when the script will influence on dataset </p>

##Simulate Twittes 
<p><b>Command to run:</b> python simulate_twittes.py</p>
  <p>Line 8 - Decide the number of lines to read before the sleep command</p>
  <p>Line 21 - Decide how long it will wait until the next reading</p>

##Generate Graph
<p><b>Command to run:</b> python graph.py</p>


