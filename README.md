## Opinionated outcome or result library for kotlin
The goal of this library was to provide an easy way to result based programming. 
The idea came during use case or business layer implementation for Android mobile applications.

These do often combine different sources of data to a new data structure, but due to network 
or internal errors could often fail in multiple ways. This library uses shortcuts when 
errors occur to be able to do "railroad-programming". So every time a error happened everything
else after it will not be executed and the error can be returned or mapped to a more specific 
error class.


### How to use

#### Recommended or intended usage
 
The library contains a builder function 'outcome {}' which should cover most uses. It provides 
multiple functions to execute other outcome based functions, e.g. from repositories or data sources.
