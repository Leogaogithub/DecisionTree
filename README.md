# DecisionTree
Implemented Decision Tree based classification algorithm which can be used on any dataset to predict class of a data given

## Compile and run this project using eclipe 
* Import this project into eclipse.
* Export this project to 'Runnable JAR file'
* Run this project in command line

	> java -jar DecisionTree.jar /home/leo/workspace/DecisionTreeAssignment/data/train.dat /home/leo/workspace/DecisionTreeAssignment/data/test.dat 0.2

## compile and run this project in commandline
* Make sure to let 'DecisionTreeAssignment/src/' as current directory
* Compile this project using following command:
> javac -cp . ./leo/*.java

* Run this project using following command:

> java leo.DecisionTree /home/leo/workspace/DecisionTreeAssignment/data/train.dat /home/leo/workspace/DecisionTreeAssignment/data/test.dat 0.2

## Assumptions that we made.
* For pruning the tree, root of DecisionTree is not allowed to pruned.
* For pruning the tree, if one treeNode chosen is removed, then all its childern will be removed.

## Tasks we accomplished
* Implemented ID3 algorthim
* Implemented Pruning of the Decision Tree

## Things we learned from this project
* Understood how the ID3 algorithm works
* Understood how to apply Decision tree to the problem in real life

