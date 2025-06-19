## Opinionated outcome or result library for kotlin

The goal of this library was to provide an easy way to result based programming.
The principles originated from use case or business layer implementation principles for Android mobile applications.

These do often combine different sources of data to a new data structure, but due to network
or internal errors could often fail in multiple ways. This library uses shortcuts when
errors occur to be able to do "*railroad-programming*". So every time a error happened everything
else after it will not be executed and the error can be returned or mapped to a more specific
error class.

Other frameworks or libraries, e.g. Arrow, will provide a similar or even much wider functionality for using this functional monad principles.
So this library is more heavily influenced on a domain driven understanding of implementing the business and repository layers in applications.
Nevertheless, it is obviously heavily influenced by Arrow's Either and most usages will be even congruent.
It is mostly a opinion based, cherry-picked and more directly named version, focusing on the very minimum
function set.

---

### Usage

All revolves around the **Outcome** class. It is a generic sealed class with can either be *Success* or *Error*,
but never both. This is the whole concept of a functional monade. Other libraries call it *Either* for example,
but Outcome was a better fit, since it is not widely used and is more descriptive of what its intentions are.

#### 1. Builder function

The library contains a builder function '**outcome {}**' which should cover most use cases. This function
will return a Outcome object with either provided generic types or processed types from the code itself.
It will rarely be necessary to create a Outcome instance directly, which was one of the main goals.

It provides multiple functions to execute other outcome based functions, e.g. from repositories or data sources.

The **outcome** function has only one parameter, which is providing a DSL style approach to process any outcome
based calls inside it.

The main functions provided are
##### **validate**

```
validate provides a boolean check lambda and an error mapping lambda
```
##### **execute**
```
execute provides an action to call any outcome based code and an error mapping lambda
```
##### **raise**
```
raise provides an manual way to return an error inside the builder block
```

#### 1.1 Example
Easiest way to describe the intended usage is a proper example.
Think of an usecase class which does some sort of data fetching through multiple repositories.

##### Provided are three different repositories:
- User preferences (to get any kind of user selected settings, which can determine how to handle some data)
- Local data repository
- Remote data repository

##### Additionally, these two types are defined as Success and Error types, which will be used for the outcome:
```
data class SuccessData(val text: String)

sealed class DomainError {	
  data object UserPreferencesError: DomainError()
	data object LocalError: DomainError()
	data object RemoteError: DomainError()
}
```

##### The UseCase class would implement something like this invoke function:
```
fun invoke(): Outcome<SuccessData, DomainError> = outcome {
	// First, fetch the user preferences
	val userToggle: Boolean = execute(
		action = { userPreferences.getToggle() } ,
		onError = { DomainError.UserPreferencesError } 
	)

	// Use toggle 
	val text = if (userToggle) {
		execute(
			action = { localDataRepository.getData() },
			onError = { DomainError.LocalError },
		)	
	} else {
		execute(
			action = { remoteDataRepository.getData() },
			onError = { DomainError.RemoteError },
		) 
	}

	// Explicit return not necessary, but for the sake of this example easier to understand
	text
}
```
##### Just a brief summary, since the could is hopefully self-explanatory:
Any execute will only return the expected value if the action is a *Success*. Otherwise the the error will be raised directly after
mapping it to the proper type in *onError*. This makes it possible to use the values directly without any unwrapping or similar.
This example just calls three different functions, which themselves are Outcome based.
```
Hint: 
You can always call anything in the *action* block, 
even if it's not outcome base and just call any wrapping function to make it a Outcome, 
e.g. ".asSuccess()".
```

#### 2. Direct instance creation

Should it happen to be necessary to create Outcome instances directly,
there are multiple extension functions, which can be used to create these instances in a more concise manner.

##### 2.1. Create a Success or Error instance from any data type, without providing a Error type.
Create an object instance of *Success* or *Error* just by calling one extension function directly on it.

```
// Short extension functions
val success = "Any object".asSuccess()
val error   = "Any object".asError()

// Specified extension functions
val successOutcome = "Any object".asOutcomeSuccess()
val errorOutcome = "Any object".asOutcomeError()
```
Hint: These instances do have a *Nothing* success or error type, depending on which case was created.

##### 2.2 Create a fully typed Success or Error instance
Create an object instance of *Success* or *Error* just by calling one extension function directly on it.
It is necessary to provide both Success and Error generic types.

```
val typedSuccess = "Any object".asTypedSuccess<String, *Error*>()
val typedError   = "Any object".asTypedError<*Success*, String>()
```

#### 3. Pitfalls
##### 3.1 Using Unit as generic type for error.
When using Unit as generic type for error, but returning a different error type in any execute, the returned error will not be correctly handled, but Unit will be returned. If using Unit as a generic type, be aware of the implications, that other types inside the builder function error mapping might be not returned, but converted to Unit instead.



