# PPM-Java-Code-used-during-Ph.D.


TITLE: PPM Star Java Code

AUTHOR: Liam Jordon

PURPOSE:

This project contains Java code of a simplified implementation of the PPM Star compression algorithm. 
It is not to be treated as an actual recreation of PPM Star. It was used to collect data on how PPM Star behaves on specific binary strings for my Ph.D. thesis.


It led to the paper 'A Normal Sequence Compressed by PPM* But Not by Lempel-Ziv 78'.
The paper can be found here: [https://link.springer.com/chapter/10.1007/978-3-030-67731-2_28]

The data collected from the project also made up Chapter 7 of my Ph.D. thesis.
The thesis can be accessed here: [https://mural.maynoothuniversity.ie/16566/1/Liam_Jordon_PhDThesis.pdf]

On input of a binary string, PPM.encode displays how the input string would be compressed by PPM Star: 
    i.e. - How the compressor builds its model at each stage.
         - Which contexts are used to encode the current character being encoded.
         - What are the prediction probabilities at each stage.
         

A copy of the paper which originally describes PPM Star can be found here: [https://www.cs.waikato.ac.nz/~ihw/papers/95JC-WT-IHW-Unbound.pdf]

DATE: The paper which was published based on data from this code was published in January 2021.

USER INSTRUCTIONS: 

Running the main method in the main class file will show how a specific string is compressed by PPM Star.

The string compressed is: 

000111010001110100011101

It will then ask the user for a binary string of their choice to compress.
