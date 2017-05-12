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
  - they are not case sensitive but generally spelled uppercase for making them more readable
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
  893H1TZ714CYAHIGV8ESUCGWDZ {8846de00-26dc-4e86-ac23-24a352cb0271}
  99BVPTSZS4L#9ESYZT3E2SUBT7 {8c354b87-32bb-49b1-a71b-591d29b87372}
  62HRXZAHLHTZS3*SFRYDTL5V# {00afe623-09dd-429d-b54e-2b355c7e4076}
  3EQH99NEI84CKIZ99X5GK9D3*S {e70ed4cf-00f1-4645-94b0-975138b9be26}
  3PSTUJCLUAVS252TAHJXG4TIT {0b23f9b9-a99f-40d5-851c-6864c5b518ef}
  2DNMQ7YMV*KNMZX6C#J9BPPWVU {d54c9d4e-4165-4448-811b-a188233fdad0}
  1A2#3XY7GC7F8*5WABL4ABQWY* {98ef2718-98a0-48ba-b963-99becec9f083}
  28DHXJW4EGIFRHJMIWK179P9V2 {7c16b8b2-c0ce-4fb3-978d-b94c64ce2c55}
  469A7IT6BKC#X2RQIAT1UZTT#W {58544a3d-0f48-47c9-b47b-9d7cc5293756}
  34EIQDQR8L2UGQNL7Y8MLC2E5N {3900eb1f-0dae-4b18-a5a5-6f05dfa7c3b6}
  5ARPGKDHLELQP1LIIRP1TJGA47 {a40e6370-6a03-404e-84ae-10fca8ec39c2}
 
 
 ### Advantages over Base64 encoded GUIDs ###
  Advantages over Base64 encoded GUIDs are among others:
 
  - Base64 GUIDs are 22 chars instead of 25-26 of GUID-B, not that shorter
  - GUID-B could also be 22 char long but then it would need to mix upper and lower case as Base64 GUIDs thus jeopardizing readability
  - Base64 GUIDs contain :\%$ symbols which could be confusing while GUID-B only * and # symbols
  - Base64 GUIDs contain 0,O,o symbols which could be confusing and also 1,l, GUID-B doesn't
  - Base64 GUIDs have no error detection as opposed to GUID-B's
 
### Installation ###

This project was created with NetBeans 8 and is a maven project, so it should be a matter of opening it with NetBeans and running the GUIDB main method.

### Uses ###

It has been used for outputting meaningful tags for errors which could then be traced back to the code, it's been used for quickly searching documents and 
relations among them, basically whenever a GUID is needed this shorter and more compact version has been used.

### Versioning ###

Git was used for versioning. For the versions available, see the tags on this repository.

### Authors ###



    