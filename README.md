-LuceneIndex
============
This is a Java based project.




Prerequisites:

java sdk 7

https://lucene.apache.org/

http://code.google.com/p/json-simple/


Data:

https://www.yelp.com/academic_dataset


Buid and run:

We recommend NetBean as your IDE.

If you have NetBean, File->Open directory, that's all.

You can always use ant to build the project.

```
$ant
```

Usage:

A) Index and retrive:

To build indexes please execute buildindex.java one need to change the input JSON file location. The output will bin in myindex folder, around 200MB. To query the system, please run loadIndex.java.

Original and analyzer processed query is print out. Time used to query system is recorded as well. Rank and matched key word are highlighted. A snippet as well as the full review is shown to user. 

B) Test analyzers:

Eight anlyzers are included in the project. Please check eval_run_experiment.java for details. Testing results will be in *.csv file.





