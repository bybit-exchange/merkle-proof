package com.bybit.merkle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerkleTree {
    private Self self;
    private List<Path> path;

    public boolean validate(){
        if(this.self == null || this.path == null || this.path.isEmpty()) {
            return false;
        }
        if(!this.self.validate()) {
            return false;
        }
        if(!path.get(0).validate()) {
            return false;
        }
        if(path.get(0).getType() == null || Objects.equals(path.get(0).getType(),self.getType())) {
            return false;
        }
        String left, right;
        if(self.getType() == 1){//left
            left = self.getHash();
            right = path.get(0).getHash();
        } else { //right
            left = path.get(0).getHash();
            right = self.getHash();
        }
        int height = 1;
        Path node = Path.instance(left, right, self.getBalance(), path.get(0).getBalance(), height + 1, getTypeBySibling(path.get(1)));
        for(int i = 1; i < path.size() - 1; i++){
            height++;
            if(!path.get(i).validate()) {
                return false;
            }
            if(!node.validate()) {
                return false;
            }
            Path currentSibling = path.get(i);
            if(currentSibling.getType() == 1) {
                left = currentSibling.getHash();
                right = node.getHash();
            } else {
                left = node.getHash();
                right = currentSibling.getHash();
            }
            Integer type = getTypeBySibling(path.get(i + 1));
            node = Path.instance(left, right, node.getBalance(), path.get(i).getBalance(), height + 1, type);

        }
        Path root = path.get(path.size() - 1);

        if(!node.getHash().equals(root.getHash()) || !node.getBalance().equals(root.getBalance()) || !Objects.equals(node.getHeight(), root.getHeight())) {
            return false;
        }
        return true;
    }

    private Integer getTypeBySibling(Path path){
        Integer siblingType = path.getType();
        return siblingType == 3 ? 3 : siblingType == 1 ? 2 : 1;
    }
}
