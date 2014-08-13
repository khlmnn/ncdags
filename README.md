# Tabulation of noncrossing acyclic digraphs

A *noncrossing graph* is a graph with its vertices drawn on a circle and its edges drawn in the interior such that no two edges cross each other.
This repository contains a note describing a tabulation technique for *noncrossing acyclic digraphs*.

The note presents an algorithm that, given a number *n*, computes a compact representation of the set of all noncrossing acyclic digraphs with *n* nodes.
This compact representation can be used as the basis for a wide range of dynamic programming algorithms on these graphs.

As an illustration, I release the implementation of an algorithm for counting the the number of noncrossing acyclic digraphs of a given size.
This number is given by the following integer sequence:

	1, 3, 25, 335, 5521, 101551, 1998753, 41188543, 877423873, 19166868607, ...

This sequence was first described in an answer to [a question that I asked on MathOverflow](http://mathoverflow.net/questions/176944/).

Another application of the tabulation technique is in semantic dependency parsing, where it can be used to compute the highest-scoring dependency graph for a given sentence under an edge-factored model.

## License

<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" rel="dct:type">work</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://www.ida.liu.se/~marku61/" property="cc:attributionName" rel="cc:attributionURL">Marco Kuhlmann</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.<br />Based on a work at <a xmlns:dct="http://purl.org/dc/terms/" href="https://github.com/khlmnn/ncdags" rel="dct:source">https://github.com/khlmnn/ncdags</a>.
