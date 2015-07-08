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
```sh
spark-submit --master local[4] social-networks-monitor-alarm-idea-project.jar \
consumerKey consumerSecret accessToken accessTokenSecret \
dictfilenm \ 
outdir \
historicinterval \
recenticinterval \
stubport \
limit_stdev_cof \
```
Parameters:
- `consumerKey` `consumerSecret` `accessToken` `accessTokenSecret`  - Twitter Tokens
- `dictfilenm` - File path of a dictionary with company's categories terms
- `outdir` - Directory where output files will be placed
- `historicinterval` - Historical interval in minutes (int)
- `recenticinterval` - Recent interval in minutes (int)
- `stubport` - Port number where stub stream will be listening.Set *0* to disable (int)  
- `limit_stdev_cof`- Coefficient of historic stdev to trigger alarm  (float)

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


