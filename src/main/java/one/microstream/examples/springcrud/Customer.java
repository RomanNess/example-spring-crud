
package one.microstream.examples.springcrud;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
	long customerNumber;
	String firstName;
	String lastName;
}
