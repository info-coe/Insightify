import com.infomericainc.insightify.manager.ResponseFormatter
import org.junit.Test

class RegexChatManagerTest {

    @Test
    fun `test matched with valid response`() {
        val response = """
 Question 1 : What is the main difference between an interface and an abstract class in Java?
                                                                                                    Answer: An interface is a reference type in Java, similar to a class, that can contain only constants, method signatures, default methods, static methods, and nested types. Abstract classes, on the other hand, are classes that contain one or more abstract methods, which are declared, but not implemented in the abstract class. 
Code: 
```java
// Abstract class
abstract class Animal {
abstract void makeSound();
}
                                                                                                    
                                                                                                    // Interface
                                                                                                    interface Animal {
                                                                                                      void makeSound();
                                                                                                    }
                                                                                                    ```
                                                                                                    Reference: Comparison between Interface and Abstract class in Java.
                                                                                                    ReferenceLink: https://www.geeksforgeeks.org/difference-between-abstract-class-and-interface-in-java/
                                                                                                    
                                                                                                    
Question 1 : What is the main difference between an interface and an abstract class in Java?
                                                                                                    Answer: An interface is a reference type in Java, similar to a class, that can contain only constants, method signatures, default methods, static methods, and nested types. Abstract classes, on the other hand, are classes that contain one or more abstract methods, which are declared, but not implemented in the abstract class. 
Code: 
```java
// Abstract class
abstract class Animal {
abstract void makeSound();
}
                                                                                                    
                                                                                                    // Interface
                                                                                                    interface Animal {
                                                                                                      void makeSound();
                                                                                                    }
                                                                                                    ```
                                                                                                    Reference: Comparison between Interface and Abstract class in Java.
                                                                                                    ReferenceLink: https://www.geeksforgeeks.org/difference-between-abstract-class-and-interface-in-java/
        """.trim()

        val chatManager = ResponseFormatter().setResponse(response)
    }

}