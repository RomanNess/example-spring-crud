
package one.microstream.examples.springcrud;

public class Customer
{
	private String firstName;
	private String lastName;
	private Long   customerNumber;
	
	public Customer(final String firstName, final String lastName, final Long customerNumber)
	{
		super();
		this.firstName      = firstName;
		this.lastName       = lastName;
		this.customerNumber = customerNumber;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public Long getCustomerNumber()
	{
		return this.customerNumber;
	}
	
	public void setCustomerNumber(final Long customerNumber)
	{
		this.customerNumber = customerNumber;
	}
	
	@Override
	public String toString()
	{
		return "Customer [firstName=" + this.firstName + ", lastName=" + this.lastName + ", customerNumber="
			+ this.customerNumber + "]";
	}
}
