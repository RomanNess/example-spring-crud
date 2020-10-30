
package one.microstream.examples.springcrud;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Qualifier("synchronizedCustomerRepository")
public class SynchronizedCustomerRepositoryImpl implements CustomerRepository
{
	private final List<Customer>         customers;
	private final EmbeddedStorageManager storage;

	public SynchronizedCustomerRepositoryImpl(@Value("${microstream.store.location.synchronized}") final String location)
	{
		super();

		this.customers = Collections.synchronizedList(new ArrayList<>());

		this.storage   = EmbeddedStorage.start(
			this.customers,
			Paths.get(location)
		);
	}

	@Override
	public void storeAll()
	{
		this.storage.store(this.customers);
	}

	@Override
	public void add(final Customer customer)
	{
		this.customers.add(customer);
		this.storeAll();
	}

	@Override
	public List<Customer> findAll()
	{
		return this.customers;
	}

	@Override
	public void deleteAll()
	{
		this.customers.clear();
		this.storeAll();
	}

	@Override
	public List<Customer> findByFirstName(final String firstName)
	{
		return this.customers.stream()
			.filter(c -> c.getFirstName().equals(firstName))
			.collect(Collectors.toList());
	}
}
