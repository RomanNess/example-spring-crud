package one.microstream.examples.springcrud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void concurrentUpdate_causesConcurrentUpdateException() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Future<Boolean>> futures = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Future<Boolean> future = executorService.submit(() -> {

                Customer newCustomer = Customer.builder().build();

                customerRepository.findAll().add(newCustomer);
                customerRepository.storeAll();

                return true;
            });

            futures.add(future);
        }

        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertThatThrownBy(() -> {
            for (Future<Boolean> future: futures) {
                future.get();
            }
        })
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(ConcurrentModificationException.class)
        ;
    }

}