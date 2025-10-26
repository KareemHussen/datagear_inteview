package com.datagear.DataGearInterview.config;

import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import com.datagear.DataGearInterview.repository.TransactionRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Database seeder to populate the database with random transaction data
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final TransactionRepository transactionRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting database seeding...");

        // Check if data already exists
        if (transactionRepository.count() > 0) {
            log.info("Database already contains data, skipping seeding");
            return;
        }

        List<Transaction> transactions = generateRandomTransactions(1000);
        transactionRepository.saveAll(transactions);

        transactions.forEach(t -> {
            t.setCreatedAt(generateRandomDateTime());
        });

        transactionRepository.saveAll(transactions);

        log.info("Database seeding completed! Generated {} transactions", transactions.size());
    }

    /**
     * Generate random transactions using JavaFaker
     * @param count number of transactions to generate
     * @return list of random transactions
     */
    private List<Transaction> generateRandomTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Transaction transaction = Transaction.builder()
                    .amount(generateRandomAmount())
                    .type(generateRandomType())
                    .note(generateRandomNote())
                    .createdAt(generateRandomDateTime())
                    .build();
            
            transactions.add(transaction);
        }
        
        return transactions;
    }

    /**
     * Generate random amount between 1.00 and 10000.00
     */
    private BigDecimal generateRandomAmount() {
        double amount = faker.number().randomDouble(2, 100, 1000000) / 100.0; // Convert to decimal
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Generate random transaction type (Credit or Debit)
     */
    private TransactionType generateRandomType() {
        return random.nextBoolean() ? TransactionType.CREDIT : TransactionType.DEBIT;
    }

    /**
     * Generate random note using JavaFaker
     */
    private String generateRandomNote() {
        String[] noteTemplates = {
            "Salary payment",
            "Rent payment",
            "Grocery shopping",
            "Gas station",
            "Online purchase",
            "ATM withdrawal",
            "Service fee",
            "Interest payment",
            "Dividend received",
            "Investment return"
        };

        return noteTemplates[faker.number().numberBetween(0 , noteTemplates.length - 1)];
    }

    /**
     * Generate random date time within the last 6 months
     */
    private LocalDateTime generateRandomDateTime() {
        return faker.date().between(
            java.sql.Date.valueOf(LocalDateTime.now().minusMonths(13).toLocalDate()),
            java.sql.Date.valueOf(LocalDateTime.now().toLocalDate())
        ).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
}
