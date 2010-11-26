CPAR Classifier
===
Dietrich Featherston, `fthrstn2@illinois.edu`, `d@d2fn.com`

This module contains an implementation of a CPAR classifier based on the paper by Yin and Han [1].  The implementation is capable of testing classifier effectiveness against training and test data sets and predicting labels for unlabled data based on provided training data.

The algorithm generates predictive association rules based on all unique class labels found in training data sets.  Rules are incrementally built up by selecting the next best predicate that leads to the greatest reduction in entropy of the training data with respect to the class label currently being mined.  CPAR also selects rules with similar gain so that important rules are not overlooked.  As rules are generated, the weight of all transactions matching the new rule have their weighting decayed by a percentage.  This allows new rules to be generated which address transactions not covered by existing rules (or covered by fewer rules).  This leads to a greater number of rules being generated which helps to improve classifier accuracy.  This works because when information gain is calculated it takes into account the weight of each transaction matching a given class label instead of treating them all equally.

When choosing which label to predict for an example, we select the best _k_ rules for every available class label based on the _Laplace Accuracy_ of each.  The average accuracy of the rules for each label is computed and the label for the set with the highest value is given as the predicted label.


Building
---
Enter the root directory and run `ant` to compile this program.  The programming environment requires Java 6 and a recent version of Ant.  There are no other special requirements.  The environment used is Java 1.6.0 R22 from Apple and Apache Ant 1.8.1 on a MacBook Pro.

Running
---
The classifier may be run in two modes, `test` and `predict`, covered here.

Given two labeled data sets, one for training and one for testing, the following command will predict the labels of the test set and output the accuracy.

	> ./test [trainingFile] [testFile]

The following command will predict the labeles of an unlabeled data file given a training set of labeled data.  The labels are written, one per line, to a separate file.

	> ./predict [labeledData] [unlabeledData] [outputLabels]


Performance Analysis
---

For these tests, we have some parameters to set on the classifier as outlined in [1].  These are not made available as parameters to the classifier at this time.

* Rules per class label (_k_): 6
* totalWeightFactor: 0.001
* decayFactor: 0.25
* minGain: 1.0

Let's take a look at the performance of this CPAR implementation both in running time and accuracy for the included data sets.  First let's consider data set 1 which is split up into the training data `train1.txt` and the test data `test1.txt`.  This data set contains 39,074 labeled training examples and 9,768 labeled test examples. We run the following command to test the classifier against this data set.

	> ./test train1.txt test1.txt

We will see some output scroll by showing the association rules being generated and the _Laplace Accuracy_ of each.  For this data set roughly 60 rules will be generated and the entire processing time was about 6 minutes on a 2.66 GHz Intel Core i7.  The following illustrates the classifier's accuracy showing positive and negative examples, those with labels of "1" and "0", respectively.

	[P=7431,N=2337][TP=4915,TN=2129,FP=208,FN=2513,U=3]

Here we see that the accuracy of the classifier in terms of the percentage of correctly predicted labels is `(TP+TN)/(P+N)` or 72.1%.

We then test the second data set by running the following command.

	> ./test train2.txt test2.txt

This data set contains 2,557 examples for training and 639 for testing.  The command completes in just a few seconds in my environment.  Results were surprisingly inaccurate for the second data set.

	[P=639,N=0][TP=97,TN=0,FP=0,FN=8,U=534]

The problem appears to be due to the high number of examples in the test data set that match no rule body generated from the training data set (83%).  The overall accuracy is 15.2%.  However, if we only measure the accuracy against examples in the test set for which a label could be guessed we see the accuracy jump to 92.4%.  It is possible that changing the classifier parameters outlined at the start of this section would lead to more rules being generated and, by extension, an increased ability to guess labels.

Labeling
---

Lastly, an example is provided demonstrating how to provide labels for the unlabeled data in the third data set.  This is done with the following command.

	> ./predict train3.txt test3_without_label.txt test3_label.txt

This produces a new file `test3_label.txt` containing predicted labels for the transactions in `test3_without_label.txt` based on rules generated using labeled data in `train3.txt`.


References
===
[1] Han. CPAR: Classification based on predictive association rules. Proceedings of the third SIAM international conference on data mining (2003) pp. 331
