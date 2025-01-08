package com.payvang.Invoice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payvang.Invoice.Entities.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long>{

}
