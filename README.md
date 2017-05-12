# GUIDB-GUID-Breve
GUIDB (GUID Breve) a two way reversible function for shortening GUIDs

### Introduction ###

 GUID-Breve or GUID-B (italian shortening for GUID Breve - "Short GUID" in english), is a shorter version of GUID (25-26 chars instead of 36) 
 which can be converted back to a GUID.
 
 One can thus decide storing GUIDs as GUID-Bs and be able in the future to convert them back to GUIDs.
 Apart from being shorter than GUIDs they have other desired characteristics:
 
  - they don't have the character 0 (zero) and O (capital o) appearing, 1 (one) and l (lowercase l) thus avoiding typing mistakes
  - they may have the * and # characters appearing making eye comparison from a distance easier
  - they have a CRC check (the first digit is CRC bit) to check validity
  - they are not case sensitive but generally spelled uppercase for making them more readable :eyeglasses:
  - they don't appear hyphened of dehyphened and with or without parentheses as with GUIDs

### Examples ###
  
  Here some examples of GUID-B and corresponding string representation of GUID:
 
  388H6BSHJ#SBREDWWQ#2QVEJFS {79bbb700-c19c-44dc-8213-045f08e3cb72}
  
  9FTNBZN48ZIU7HIDRS77SC5QHU {f96f97e6-98d2-435d-bba7-8f0f7e9fc7dc}
  
  1B5XVPUJA2GR4DT569ZKJ8Q2DG {ab341e2a-441f-4795-9821-405466347bcf}
  
  3F1CKQHPZKFDXJS1ZF7Z1X99GH {eca09766-a64a-4ef8-b24d-9c6575a8ebac}
  
  7G2E6KNX*CTZ*BVG#EFGUQFFYJ {fe02379e-81c0-40ec-a5fd-481121f9baf2}
  
  93BGVMGZQF88SWBW9U*Y7BXKVS {26ae3cf7-a8ee-4340-9687-c040bf97581e}
  
  3BIRV7RW9ULLFEA5DLXB92GJYX {b1398d69-f499-491f-90eb-07809b9e2e7f}
  
  4448762ZUN7HI#7HLU4VAE8RC* {342cb68d-676e-41ed-99f4-3f2e7f6dfcff}
 
 
 ### Advantages over Base64 encoded GUIDs ###
  Advantages over Base64 encoded GUIDs are among others:
 
  - Base64 GUIDs are 22 chars instead of 25-26 of GUID-B, not that shorter
  - GUID-B could also be 22 char long but then it would need to mix upper and lower case as Base64 GUIDs thus jeopardizing readability
  - Base64 GUIDs contain :\%$ symbols which could be confusing while GUID-B only * and # symbols
  - Base64 GUIDs contain 0,O,o symbols which could be confusing and also 1,l, GUID-B doesn't
  - Base64 GUIDs have no error detection as opposed to GUID-B's
 
### Installation ###

This project was created with NetBeans 8 and is a maven project, so it should be a matter of opening it with NetBeans and running the GUIDB main method.  :thumbsup: :thumbsup:

### Uses ###

It has been used for outputting meaningful tags for errors which could then be traced back to the code, it's been used for quickly searching documents and 
relations among them, basically whenever a GUID is needed this shorter and more compact version has been used.

### Versioning ###

Git was used for versioning. For the versions available, see the tags on this repository.

### Authors ###



    