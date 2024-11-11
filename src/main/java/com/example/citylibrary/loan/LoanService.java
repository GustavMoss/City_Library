package com.example.citylibrary.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loans> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loans> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loans createLoan(Loans loan) {
        // loan.setLoan_date(LocalDate.now());
        
        loan.setReturned_date(null);
        return loanRepository.save(loan);
    }

    public Loans updateLoan(Long id, Loans newLoan) {
        Optional<Loans> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            loan.get().setBook_Id(newLoan.getBook_Id());
            loan.get().setDue_date(newLoan.getDue_date());
            loan.get().setLoan_date(newLoan.getLoan_date());
            loan.get().setLoan_id(newLoan.getLoan_id());
            loan.get().setReturned_date(newLoan.getReturned_date());
            loan.get().setUser_id(newLoan.getUser_id());
            return loanRepository.save(loan.get());
        } else {
            return null;
        }
    }

    public Loans addReturnedDate(Long id, LocalDate returnedDate) {
        Optional<Loans> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            loan.get().setReturned_date(returnedDate);
            return loanRepository.save(loan.get());
        } else {
            return null;
        }
    }

    public void deleteLoan(Long id) {
        Optional<Loans> loanToDelete = loanRepository.findById(id);
        if (loanToDelete.isPresent()) {
            loanRepository.deleteById(loanToDelete.get().getLoan_id());
        } else {
            // Skapa en ExceptionHandler här för Loan som
            // inte existerar ???
        }
    }
}
