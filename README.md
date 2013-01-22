Metadata
========

[![Build Status](https://secure.travis-ci.org/StratusLab/metadata.png)](https://secure.travis-ci.org/StratusLab/metadata.png)

Contains the utilities for the creation and verification of virtual
machine metadata.  These metadata descriptions are managed through the
Marketplace and used by various cloud services to validate, authorize,
and configure the associated images.

Metadata Schema and Format
--------------------------

Sharing machine and disk images requires standardized, trusted metadata to
allow users to find appropriate images and to allow system administrators to
judge the suitability of them.

The metadata descriptions are in [RDF/XML format][rdfxml] and
cryptographically signed following the [XML Signature][xmlsig] specification.
The connection between the described image and the metadata description is the
image identifier based on the SHA-1 hash. The following table shows the XML
namespaces (and usual prefixes) in the metadata descriptions.

<table>
    <tr>
        <th>Prefix</th>
        <th>Namespace</th>
    </tr>
    <tr>
        <td>rdf</td>
        <td>http://www.w3.org/1999/02/22-rdf-syntax-ns#</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>http://purl.org/dc/terms/</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>http://mp.stratuslab.eu/slreq#</td>
    </tr>
    <tr>
        <td>slterms</td>
        <td>http://mp.stratuslab.eu/slterms# </td>
    </tr>
</table>


The following XML document is an unsigned example of the metadata description.
The first element is the description of the image containing information about
the image file, contained operating system, and location. It also contains the
endorsement of the information with information on who endorsed the image and
when. The email of the endorser is used as the key and is consequently a
required element of the description. A digital signature element
("xmldsig:Signature") follows the "rdf:Description" element for signed
metadata entries. (Relevant XML namespaces are given below.)

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
         xmlns:dcterms="http://purl.org/dc/terms/"
         xmlns:slreq="http://mp.stratuslab.eu/slreq#"
         xmlns:slterms="http://mp.stratuslab.eu/slterms#"
         xmlns:ex="http://example.org/"
         xml:base="http://mp.stratuslab.eu/">

  <rdf:Description rdf:about="#MMZu9WvwKIro-rtBQfDk4PsKO7_">
    <dcterms:identifier>MMZu9WvwKIro-rtBQfDk4PsKO7_</dcterms:identifier>
    <slreq:bytes>100</slreq:bytes>

    <slreq:checksum rdf:parseType="Resource">
      <slreq:algorithm>SHA-1</slreq:algorithm>
      <slreq:value>c319bbd5afc0a22ba3eaed0507c39383ec28eeff</slreq:value>
    </slreq:checksum>

    <slreq:endorsement rdf:parseType="Resource">
      <dcterms:created>2011-01-24T09:59:42Z</dcterms:created>
      <slreq:endorser rdf:parseType="Resource">
        <slreq:email>jane.tester@example.org</slreq:email>
        <slreq:subject>CN=Jane Tester,OU=...</slreq:subject>
        <slreq:issuer>CN=Jane Tester,OU=...</slreq:issuer>
      </slreq:endorser>
    </slreq:endorsement>

    <dcterms:type>machine</dcterms:type>

    <dcterms:valid>2011-07-23T10:59:42Z</dcterms:valid>

    <dcterms:publisher>StratusLab</dcterms:publisher>
    <dcterms:title>linux-with-my-apps</dcterms:title>
    <dcterms:description>A 32-bit ttylinux...</dcterms:description>

    <slterms:location>http://example.org/...</slterms:location>

    <slterms:serial-number>0</slterms:serial-number>
    <slterms:version>1.0</slterms:version>

    <slterms:hypervisor>kvm</slterms:hypervisor>

    <slterms:inbound-port>443</slterms:inbound-port>
    <slterms:outbound-port>25</slterms:outbound-port>
    <slterms:icmp>8</slterms:icmp>

    <slterms:os>ttylinux</slterms:os>
    <slterms:os-version>9.7</slterms:os-version>
    <slterms:os-arch>i486</slterms:os-arch>

    <slterms:deprecated>security issue with app</slterms:deprecated>

    <ex:other-info>additional metadata</ex:other-info>
    <ex:yet-more>still more info</ex:yet-more>

    <ex:relatedImages rdf:parseType="Resource">
      <dcterms:identifier>MMZu9WvwKIro-rtBQfDk4PsKO7_</dcterms:identifier>
      <dcterms:identifier>NMZu9WvwKIro-rtBQfDk4PsKO7_</dcterms:identifier>
      <dcterms:identifier>OMZu9WvwKIro-rtBQfDk4PsKO7_</dcterms:identifier>
      <dcterms:identifier>PMZu9WvwKIro-rtBQfDk4PsKO7_</dcterms:identifier>
    </ex:relatedImages>
  </rdf:Description>
</rdf:RDF>
```


The entries in the Marketplace deal with individual images. If it is desired
that collections of images are signed, then one possibility is to include in
each individual entry references to the other image descriptions in the
collection. This allows the full collection to be reconstructed from any
individual entry. One method of doing this is shown in the example metadata
description.

Signing and Validating StratusLab Metadata Files
------------------------------------------------

For signing and validating metadata files we are using [XML Signature][xmlsig]
specification. Commands to support metadata signatures have been written in
Java as recent Java virtual machines contain an API implementing this
standard.

Metadata files can be signed using grid certificates (in PKCS12 format), PGP
key pairs, or DSA/RSA key pairs. Verification and validation automatically
detects signature algorithm and type of private key used for signing metadata
files, verifies the metadata file and prints, for grid certificates, the DN of
the user who signed the metadata file.

Metadata Elements
-----------------

Where possible the [Dublin Core metadata vocabulary][dublincore] has been used
for the metadata description. The following table shows the terms taken from
the Dublin Core specification.

<table>
    <tr>
        <th>NS</th>
        <th>qname</th>
        <th>freq.</th>
        <th>XSD></th>
        <th>Constraints</th>
        <th>Notes</th>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>identifier</td>
        <td>1</td>
        <td>string</td>
        <td>valid identifier</td>
        <td>image identifier</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>isReplacedBy</td>
        <td>?</td>
        <td>string</td>
        <td>valid identifier</td>
        <td>image identifier for replacement image</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>replaces</td>
        <td>?</td>
        <td>string</td>
        <td>valid identifier</td>
        <td>image identifier for image replaced by this one</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>isVersionOf</td>
        <td>?</td>
        <td>string</td>
        <td>valid identifier</td>
        <td>image identifier for parent image</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>valid</td>
        <td>?</td>
        <td>dateTime</td>
        <td>XML DateTime format</td>
        <td>expiration date for image metadata</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>title</td>
        <td>?</td>
        <td>string</td>
        <td></td>
        <td>short title for humans</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>description</td>
        <td>1</td>
        <td>string</td>
        <td></td>
        <td>longer description of the image</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>type</td>
        <td>1</td>
        <td>string</td>
        <td>'machine' or 'disk'</td>
        <td>type of the described image</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>creator</td>
        <td>?</td>
        <td>string</td>
        <td></td>
        <td>name of image or metadata record creator</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>created</td>
        <td>?</td>
        <td>dateTime</td>
        <td>XML DateTime format</td>
        <td>date when metadata record was created</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>publisher</td>
        <td>?</td>
        <td>string</td>
        <td></td>
        <td>publisher (group, experiment, project) of image</td>
    </tr>
    <tr>
        <td>dcterms</td>
        <td>format</td>
        <td>1</td>
        <td>string</td>
        <td></td>
        <td>format of machine or disk image</td>
    </tr>
</table>

Additional terms have been defined by StratusLab to complete the metadata
description. The following table shows those terms.

<table>
    <tr>
        <th>NS</th>
        <th>qname</th>
        <th>freq.</th>
        <th>XSD></th>
        <th>Constraints</th>
        <th>Notes</th>
    </tr>
    <tr>
        <td>slreq</td>
        <td>endorsement</td>
        <td>1</td>
        <td>complex</td>
        <td></td>
        <td>endorsement information</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>endorser</td>
        <td>1</td>
        <td>complex</td>
        <td></td>
        <td>endorser information</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>bytes</td>
        <td>1</td>
        <td>positive integer</td>
        <td></td>
        <td>number of bytes in described image</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>checksum</td>
        <td>+</td>
        <td>string</td>
        <td>lowercase hex digits only</td>
        <td>checksum in hex with algorithm prefix</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>email</td>
        <td>1</td>
        <td>string</td>
        <td></td>
        <td>email address of the metadata record creator</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>subject</td>
        <td>1</td>
        <td>string</td>
        <td></td>
        <td>certificate subject</td>
    </tr>
    <tr>
        <td>slreq</td>
        <td>issuer</td>
        <td>+</td>
        <td>string</td>
        <td></td>
        <td>certificate issuer</td>
    </tr>
</table>

Additional terms can be added to the metadata descriptions, but they should
appear in their own XML namespaces. This allows for application-specific
metadata and also evolution of the standard schema. These should appear after
the endorsement element in the description.


License
-------

Licensed under the Apache License, Version 2.0 (the "License"); you
may not use this file except in compliance with the License.  You may
obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied.  See the License for the specific language governing
permissions and limitations under the License.

Acknowledgements
----------------

This software originated in the StratusLab project that was co-funded
by the European Community’s Seventh Framework Programme (Capacities)
Grant Agreement INFSO-RI-261552 and that ran from June 2010 to May
2012.

[rdfxml]: http://www.w3.org/TR/2004/ REC-rdf-syntax-grammar-20040210/
[xmlsig]: http://www.w3.org/TR/2008/REC-xmldsig-core-20080610/
[dublincore]: http://dublincore.org/documents/2010/10/11/dcmi-terms/


