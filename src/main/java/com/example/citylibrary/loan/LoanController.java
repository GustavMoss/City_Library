package com.example.citylibrary.loan;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<Loans>> getAllLoans() {
        List<Loans> loans = loanService.getAllLoans();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Optional<Loans>> getLoanById(@PathVariable Long id) {
        Optional<Loans> loan = loanService.getLoanById(id);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Loans> createLoan(@RequestParam Long bookId, @RequestParam Long userId ) {
        Loans loan = loanService.createLoan(bookId, userId);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loans> updateLoan(@PathVariable Long id, @RequestBody @Valid Loans loan) {
        Loans updatedLoan = loanService.updateLoan(id, loan);
        return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Loans> addReturnedDate(@PathVariable Long id) {
        Loans loanDate = loanService.addReturnedDate(id);
        return new ResponseEntity<>(loanDate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok("Successfully deleted the loan");
    }
}
