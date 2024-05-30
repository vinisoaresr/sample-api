package dev.vinicius.infra.data;

import dev.vinicius.application.TransferRepository;
import dev.vinicius.entity.Transfer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DatabaseTransferRepository implements TransferRepository, PanacheRepositoryBase<Transfer, String> {
    @Override
    public Transfer findTransferById(String transferId) {
        return this.findById(transferId);
    }

    @Override
    public void save(Transfer transfer) {
        this.persist(transfer);
    }

    @Override
    public void removeAll() {
        this.deleteAll();
    }

    @Override
    public List<Transfer> findAllTransfers() {
        return this.listAll();
    }


}
