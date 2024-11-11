package com.example.citylibrary.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @GetMapping
    public List<Loans> getAllLoans(){
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    public Optional<Loans> getLoanById(@PathVariable Long id){
        return loanService.getLoanById(id);
    }

    @PostMapping
    public Loans createLoan(@RequestBody Loans loan){
        return loanService.createLoan(loan);
    }

    @PutMapping("/{id}")
    public Loans updateLoan(@PathVariable Long id, @RequestBody Loans loan){
        return loanService.updateLoan(id, loan);
    }

    @PutMapping("/return/{id}")
    public Loans addReturnedDate(@PathVariable Long id, @RequestBody LocalDate returnedDate){
        return loanService.addReturnedDate(id, returnedDate);
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
    }
}
