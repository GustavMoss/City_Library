package com.example.citylibrary.admin.loans;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/loans")
public class LoanAdminController {

    private LoanService loanService;

    public LoanAdminController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Gets all the loans both inactive and active
    @GetMapping("/get-all-loans")
    public ResponseEntity<List<Loans>> getAllLoans() {
        List<Loans> loans = loanService.getAllLoans();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/get-active-loans")
    public ResponseEntity<List<Loans>> getActiveLoans() {
        return loanService.getAllActiveLoans();
    }

    @PostMapping("/create-new-loan")
    public ResponseEntity<Loans> createLoan(@RequestParam Long bookId, @RequestParam Long userId ) {
        Loans loan = loanService.createLoan(bookId, userId);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @PutMapping("/{loanId}/return-loan")
    public ResponseEntity<Loans> addReturnedDate(@PathVariable Long loanId) {
        Loans loanDate = loanService.addReturnedDate(loanId);
        return new ResponseEntity<>(loanDate, HttpStatus.OK);
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.ok("Successfully deleted the loan");
    }
}
