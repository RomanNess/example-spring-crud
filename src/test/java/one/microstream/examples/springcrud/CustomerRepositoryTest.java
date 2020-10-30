package one.microstream.examples.springcrud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void initData() {
        customerRepository.deleteAll();

        long customerNr = 1L;
        customerRepository.add(new Customer(customerNr++, "Thomas", "Wresler"));
        customerRepository.add(new Customer(customerNr++, "Jim"   , "Joe"));
        customerRepository.add(new Customer(customerNr++, "Kamil" , "Limitsky"));
        customerRepository.add(new Customer(customerNr++, "Karel" , "Ludvig"));

        // data is not deleted intentionally
    }

    @Test
    void findAll() {
        assertThat(customerRepository.findAll())
                .hasSize(4)
                .extracting(Customer::getFirstName).containsExactlyInAnyOrder("Thomas", "Jim", "Kamil", "Karel");
    }

    @Test
    void findByFirstName() {
        assertThat(customerRepository.findByFirstName("Jim"))
                .containsExactly(Customer.builder().customerNumber(2L).firstName("Jim").lastName("Joe").build());
    }

    @Test
    void updateAll() {
        customerRepository.findAll().forEach(c -> c.setFirstName("Johan"));
        customerRepository.storeAll();

        assertThat(customerRepository.findAll())
                .hasSize(4)
                .extracting(Customer::getFirstName).containsOnly("Johan");
    }

    @Test
    void deleteAll() {
        customerRepository.deleteAll();
        assertThat(customerRepository.findAll()).isEmpty();
    }

    @Test
    void save() {
        assertThat(customerRepository.findAll()).hasSize(4);

        Customer customer = Customer.builder().customerNumber(1L).firstName("Hans").lastName("Wurst").build();
        customerRepository.add(customer);

        assertThat(customerRepository.findAll())
                .hasSize(5)
                .contains(customer);
    }
}