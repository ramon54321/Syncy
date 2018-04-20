## Syncy
#### Getting Started
* Run `sbt assembly` to build the jar file.
* Run `java -jar <jarname> <mode>` in the target/scala folder to start the
application in the desired mode, where 'mode' can be one of the following.

| Mode          | Description |
| ------------- |-------------|
| cli    | Starts the interface to control any other node |
| server | Starts the main server node, which can be controlled by a cli node |

#### Conformality Norms
* All 'mode specific' classes are prepended with the mode name, eg `CliActor`, `ServerMain`.
* Prefer maps over loops

#### Contributing
Unfortunatly this is a closed project, but feel free to browse the source.