**1. Prototype URL:**

http://ec2-34-207-163-186.compute-1.amazonaws.com/

**2. Installation Guide reference (in the repository):**

Installation Guide.docx

**3. Brief description of approach used to create the prototype:**

Our approach was to provide a social platform that encouraged discussion of visualizations and data as the primary view in the tool, then allowing users live access to data.gov data sets, and then enabling users to state what type of insight they wanted, leading in turn to the type of analytics in R, followed by visualization.

**3a. Multidisciplinary team, labor categories, and levels of effort (LOE):**

Harmonia created an internal prototype team consisting of:

Technical Lead: Manish Khera (LOE: 24 hours) was responsible for overall vision of the prototype, communicating formal requests for information (RFIs), deliverables status and quality, and delivery of the prototype. He also acted as the Product Owner to ensure that the prototype met the requirements, standards, and deliverables that would be expected in a full-scale deployment of a USDA Data Visualization and Analytics project.

Scrum Master: Charles Wimberley (LOE: 56 hours) were responsible for agile sprint setup and management.

Data Analyst-Senior: Carlos Cruz-Rodriguez (LOE: 42 hours) was responsible for extraction and loading of the data sets and ensuring that we followed appropriate data management, data governance and validation processes in compliance with the Data Management Book of Knowledge (DMBOK) standards and principles.

Data Scientist-Senior: Dr. Marc Abrams (LOE: 21.5 hours) was responsible for validation of the data modeling techniques and statistical analysis used in the design and development of the predictive modeling incorporated into the prototype. Also served as backup Product Owner for the prototype.

UX/Graphics Designer: Christopher Moulton (LOE: 70 hours) was responsible as an HSI Expert for prototype design, internal usability review of development, usability test plan/conduct/analysis/ report, and personas.

Application Developer-Senior: Mohamed Gaily (LOE: 123.5 hours) was responsible for creating and updating UI features within the Analyze and visualization tab and developing the back-end supporting some of those features.

Web Developer: Jonathan Foley (LOE: 125 hours) was responsible for creating and updating UI features within the Analyze tab (a shared responsibility with Mohamed Gaily), implementing responsive web design practices &amp; styling, bug fixes, and developing both the UI and back-end of the Discussion tab feature.

Data Engineer: Randall Daly (LOE: 14 hours) was responsible for validating the data sets and ensuring that we followed appropriate data management, data governance and validation processes in compliance with the Data Management Book of Knowledge (DMBOK) standards and principles including design of the underlying data models to support the prototype.

Cloud Engineer: David Ylizarde (LOE: 30 hours) was responsible for ensuring that the AWS environment was setup and configured for the prototype to be developed with all appropriate software and operating system requirements.

**3b. Compliance to Instructions (Prototype Only) to Offerors – DA Visualization BPA (Aug 13):**

Harmonia&#39;s prototype is **fully compliant to all instructions 1-11**. Justification is outlined below by instruction number.

1. _Assigned one leader and gave that person authority and responsibility and held that person accountable for the quality of the prototype submitted._

**Status: Fully compliant**

Explanation: The Technical Lead has the authority and responsibility for reviewing the prototype work (design, development, testing, documentation) and its delivery as per the instructions.

1. _Assembled a multidisciplinary and collaborative team that includes at a minimum three of the labor categories limited to the Design Pool Labor categories to design the prototype as quoted in Attachment C. The quoter&#39;s proposed mix of labor categories and level of effort for its working prototype, as reflected in Attachment C, shall be evaluated to assess the quoter&#39;s understanding and_

_capability to supply agile delivery services._

**Status: Fully compliant**

Explanation: The prototype team consisted of 9 labor categories and their LOE&#39;s (detailed in section 3a). A scrum master was assigned who set up an agile approach using the commercial tool ice Scrum where user stories were generated, added to sprints (and broken down into discrete tasks), and peer-reviewed for completion (i.e. meets success criteria or not). Daily standups were conducted to showcase progress, bring up any road-blocks, update the sprint, and set expectations for the next standup.

1. _Understand what users need, by considering potential users in the prototype design process._

**Status: Fully compliant**

Explanation: First, the team first familiarized themselves and shared their knowledge regarding the proposal topic, responses to requests for information (RFIs) on this topic, the prototype instructions, the USDA&#39;s mission, Data.gov (its functionally and its mission), the path forward for USDA with Centers of Excellence (COE), data analytics and visualization methods. This allowed the team to understand who the users are, what resources they have at their disposal, the experiences of related websites they may have, and what their goals might be. Subsequently, this knowledge was captured as personas, which are representations of primary end-users (who are they, what are their goals, and what their knowledge, skills, and abilities (KSA&#39;s) are). The personas in turn drove a design supporting their KSA&#39;s and would help them achieve their goals.  The HSI expert guided the personas, design, and development to ensure they were user-centric and user-supportive.

1. _Used at least three &quot;human-centered design&quot; techniques or tools_

**Status: Fully compliant**

Explanation: The HSI expert utilized 5 of following techniques/tools:

1) Personas (described in #3);

2) User-interface (UI) wireframing based upon personas for the initial conceptual layout and progression that allowed developers to quickly start on the architecture and development wireframe;

3) UI mockups representing detailed developer guidance were created using PowerPoint, which allowed rapid UI prototyping, capture of team feedback, easy sharing, and portrayal of the evolving design vision to development;

4) The knowledge elicitation as the usability testing method that provided insight into users needs and the usability quality of the prototype that then lead to design changes

5) Design was created by an HSI expert who continuously reviewed design and development for adherence to the U.S. Digital Standard (from which controls, colors, the responsive grid layout, etc. were patterned after), Section 508 (specifically, accessibility to software applications), W3C best practices, and application of the HSI expert&#39;s experience in understanding and applying human-centric design with regards to the user-interface and user-experience.

1. _Created or used a design style guide and/or a pattern library._

**Status: Fully compliant**

Explanation: The HSI expert created designs as guidance for development that drew from the U.S. Digital Standard.  A custom CSS theme was created for use with the Element-UI front-end library to comply with the standard.  Font Awesome icons were used for compliant icons.

1. _Used at least three modern and open source frontend or client side web technologies._

**Status: Fully compliant**

Explanation: The prototype utilizes VueJS (front-end framework), Element-UI (re-usable component library), Axios (AJAX library), Babel (cross-browser compatibility), NPM &amp; Yarn (NodeJS package management), and Webpack (front-end builder).

1. _Performed usability tests._

**Status: Fully compliant**

Explanation:  The HSI expert planned, conducted, and analyzed feedback from a usability test aimed at understanding the usability of an alpha build of the prototype.  The test type was a knowledge elicitation conducted over a web-sharing audio/visual tool. A realistic end-user, experienced in USDA applications and conducting/consuming analytics, brought up the live prototype on their end and interacted with all its elements.  Free-form feedback was captured, and structured feedback was captured in the form of Likert-scale ratings.  The survey that was used and the findings drawn from the raw feedback are included in the repository.

1. _Used an interactive approach, where feedback informed subsequent work or versions of the prototype._

**Status: Fully compliant**

Explanation:  The usability test (knowledge elicitation) gathered feedback from a realistic end-user. That feedback influenced changes to the prototype; for example, the visual layout of the Discuss and Analyze pages changed to utilize more space and make the progression of actions clearer.  Furthermore, daily standups generated questions and feedback from the team that was taken by the HSI expert and development as changes to their work. The following day, new work and changed work was reviewed by the team using the live prototype and the evolving design PPT document. This cycle continued throughout sprints.

1. _Created a prototype that works on multiple devices and presents a responsive design._

**Status: Fully compliant**

Explanation:  Documentation is provided to install and run the prototype based on a clean Amazon Linux instance and can be used for other similar Linux variants.  The instructions were tested and refined over several iterations, resulting in multiple equivalent environments with running prototypes in AWS.

1. _Provided sufficient documentation to install and run their prototype on another machine._

**Status: Fully compliant**

Explanation:  Installation instructions were created, tested in multiple equivalent environments, and provided in the repository.

1. _Prototype and underlying platforms used to create and run the prototype are openly licensed and free of charge._

**Status: Fully compliant**

Explanation: The prototype is built on freely-available technologies: Java (libraries listed at the end of the README), Maven, Github, R, Tomcat, Apache, VueJS, Element-UI, Axios, Babel, NPM, Yarn, Webpack.

**4: Repository references and descriptions:**

A. USDA FSA Prototype Design Effort v5.ppt

Description: This file includes the workflow description, personas, and design mockups as an EXAMPLE of how the design was done, team feedback annotated as call-outs, and the design artifacts created as part of the prototype effort.

B. USDA FSA Knowledge Elicitation Survey.doc

Description: This file represents the survey that was given to the realistic end-user participant during the conduct of the knowledge elicitation usability test event.

C. USDA FSA Knowledge Elicitation Findings.ppt

Description: This file represents an analysis of the realistic end-user participant&#39;s feedback as actions that were taken in the design and development of the prototype. Included in this file are 2 screenshots of design changes that occurred as a direct result of SME feedback and have been carried forth in the delivered prototype.

E.  Prototype Business Use Cases Design Document.doc

Description: This file includes a description of how we used the prototype to explore data.gov and arrive at the business use cases we utilized in the design and development of the prototype.

F. Installation Guide.doc

Description: This file provides instructions on installation and running the prototype.

5. Java libraries list

- org.springframework.boot:spring-boot-starter-web:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot-starter:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot-autoconfigure:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot-starter-logging:jar:2.0.3.RELEASE
- ch.qos.logback:logback-classic:jar:1.2.3
- ch.qos.logback:logback-core:jar:1.2.3
- org.apache.logging.log4j:log4j-to-slf4j:jar:2.10.0
- org.apache.logging.log4j:log4j-api:jar:2.10.0
- org.slf4j:jul-to-slf4j:jar:1.7.25
- javax.annotation:javax.annotation-api:jar:1.3.2
- org.yaml:snakeyaml:jar:1.19:runtime
- org.springframework.boot:spring-boot-starter-json:jar:2.0.3.RELEASE
- com.fasterxml.jackson.core:jackson-databind:jar:2.9.6
- com.fasterxml.jackson.core:jackson-annotations:jar:2.9.0
- com.fasterxml.jackson.core:jackson-core:jar:2.9.6
- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.9.6
- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.9.6
- com.fasterxml.jackson.module:jackson-module-parameter-names:jar:2.9.6
- org.springframework.boot:spring-boot-starter-tomcat:jar:2.0.3.RELEASE
- org.apache.tomcat.embed:tomcat-embed-core:jar:8.5.31
- org.apache.tomcat.embed:tomcat-embed-el:jar:8.5.31
- org.apache.tomcat.embed:tomcat-embed-websocket:jar:8.5.31
- org.hibernate.validator:hibernate-validator:jar:6.0.10.Final
- javax.validation:validation-api:jar:2.0.1.Final
- org.jboss.logging:jboss-logging:jar:3.3.2.Final
- com.fasterxml:classmate:jar:1.3.4
- org.springframework:spring-web:jar:5.0.7.RELEASE
- org.springframework:spring-beans:jar:5.0.7.RELEASE
- org.springframework:spring-webmvc:jar:5.0.7.RELEASE
- org.springframework:spring-aop:jar:5.0.7.RELEASE
- org.springframework:spring-context:jar:5.0.7.RELEASE
- org.springframework:spring-expression:jar:5.0.7.RELEASE
- org.json:json:jar:20180130
- org.apache.poi:poi-ooxml:jar:3.17
- org.apache.poi:poi:jar:3.17
- commons-codec:commons-codec:jar:1.11
- org.apache.commons:commons-collections4:jar:4.1
- com.github.virtuald:curvesapi:jar:1.04
- org.apache.poi:poi-ooxml-schemas:jar:3.17
- org.apache.xmlbeans:xmlbeans:jar:2.6.0
- stax:stax-api:jar:1.0.1
- org.apache.poi:ooxml-schemas:jar:1.3
- org.apache.commons:commons-csv:jar:1.5
- commons-io:commons-io:jar:2.6
- org.apache.commons:commons-text:jar:1.4
- org.apache.commons:commons-lang3:jar:3.7
- com.google.guava:guava:jar:26.0-jre
- com.google.code.findbugs:jsr305:jar:3.0.2
- org.checkerframework:checker-qual:jar:2.5.2
- com.google.errorprone:error\_prone\_annotations:jar:2.1.3
- com.google.j2objc:j2objc-annotations:jar:1.1
- org.codehaus.mojo:animal-sniffer-annotations:jar:1.14
- io.rest-assured:rest-assured:jar:3.1.0
- org.codehaus.groovy:groovy:jar:2.4.15
- org.codehaus.groovy:groovy-xml:jar:2.4.15
- org.apache.httpcomponents:httpclient:jar:4.5.5
- org.apache.httpcomponents:httpcore:jar:4.4.9
- org.apache.httpcomponents:httpmime:jar:4.5.5
- org.hamcrest:hamcrest-core:jar:1.3
- org.hamcrest:hamcrest-library:jar:1.3
- org.ccil.cowan.tagsoup:tagsoup:jar:1.2.1
- io.rest-assured:json-path:jar:3.0.7
- org.codehaus.groovy:groovy-json:jar:2.4.15
- io.rest-assured:rest-assured-common:jar:3.0.7
- io.rest-assured:xml-path:jar:3.0.7
- javax.xml.bind:jaxb-api:jar:2.3.0
- org.springframework.boot:spring-boot-starter-test:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot-test:jar:2.0.3.RELEASE
- org.springframework.boot:spring-boot-test-autoconfigure:jar:2.0.3.RELEASE
- com.jayway.jsonpath:json-path:jar:2.4.0
- net.minidev:json-smart:jar:2.3
- net.minidev:accessors-smart:jar:1.2
- org.ow2.asm:asm:jar:5.0.4
- org.slf4j:slf4j-api:jar:1.7.25
- org.assertj:assertj-core:jar:3.9.1
- org.mockito:mockito-core:jar:2.15.0
- net.bytebuddy:byte-buddy:jar:1.7.11
- net.bytebuddy:byte-buddy-agent:jar:1.7.11
- org.objenesis:objenesis:jar:2.6
- org.skyscreamer:jsonassert:jar:1.5.0
- com.vaadin.external.google:android-json:jar:0.0.20131108.vaadin1
- org.springframework:spring-core:jar:5.0.7.RELEASE
- org.springframework:spring-jcl:jar:5.0.7.RELEASE
- org.springframework:spring-test:jar:5.0.7.RELEASE
- org.xmlunit:xmlunit-core:jar:2.5.1
- org.junit.jupiter:junit-jupiter-api:jar:5.2.0
- org.apiguardian:apiguardian-api:jar:1.0.0
- org.opentest4j:opentest4j:jar:1.1.0
- org.junit.platform:junit-platform-commons:jar:1.2.0
- org.junit.jupiter:junit-jupiter-engine:jar:5.2.0
- org.junit.platform:junit-platform-engine:jar:1.2.0
- org.junit.jupiter:junit-jupiter-params:jar:5.2.0
- org.junit.platform:junit-platform-launcher:jar:1.2.0
- org.junit.platform:junit-platform-runner:jar:1.2.0
- junit:junit:jar:4.12
- org.junit.platform:junit-platform-suite-api:jar:1.2.0
