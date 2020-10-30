
package one.microstream.examples.springcrud;

import java.util.List;


public interface CustomerRepository
{
	void add(Customer customer);
	
	List<Customer> findAll();
	
	List<Customer> findByFirstName(String firstName);
	
	void deleteAll();
	
	void storeAll();
}
