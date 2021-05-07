package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.BlockModel;
import tn.isetsf.bpointage.model.SqlServer.BlockModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.BlockRepository;

import java.util.List;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;
    /*public void storeBlock(BlockModelSqlServer b)
    {
        BlockModel block=new BlockModel();
        block.setId(b.getCode_block());
        block.setNom_block(b.getNom_block());
    }*/
    public BlockModel storeBlock(BlockModel b)
    {
        return blockRepository.save(b);
    }
    public BlockModel getBlock(int id)
    {
        return blockRepository.findById(id).orElse(null);
    }
    public  BlockModel toBlock(BlockModelSqlServer b)
    {
        BlockModel block=new BlockModel();
        block.setNom_block(b.getNom_block());
        System.out.println(b.getCode_block());
        block.setId(b.getCode_block());
        return block;
    }
    public List<BlockModel> getAllBlocksbyDepartement(int id_departement)
    {
        return blockRepository.findAllById_departement(id_departement);
    }
    public List<BlockModel> getAll()
    {
        return blockRepository.findAll();
    }
    public BlockModel store(BlockModelSqlServer b)
    {
        BlockModel block=new BlockModel();
        block.setNom_block(b.getNom_block());
        block.setId(b.getCode_block());
        return blockRepository.save(block);
    }

    public List<BlockModel> getAllBlock() {
        return blockRepository.findAllblock();
    }

    public List<BlockModel> getBlocks() {
        return blockRepository.getBlocks();
    }
}
