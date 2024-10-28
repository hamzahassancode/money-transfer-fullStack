package com.progress.induction.cliqtransfers.repository;

import com.progress.induction.cliqtransfers.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
