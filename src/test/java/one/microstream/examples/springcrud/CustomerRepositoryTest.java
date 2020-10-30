package one.microstream.examples.springcrud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save() {
        assertThat(customerRepository.findAll()).isEmpty();

        Customer customer = Customer.builder().customerNumber(1L).firstName("Hans").lastName("Wurst").build();
        customerRepository.add(customer);

        assertThat(customerRepository.findAll()).containsExactly(customer);
    }
}