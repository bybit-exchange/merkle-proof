package com.bybit;

import com.bybit.merkle.generic.Balance;
import com.bybit.merkle.generic.Path;
import com.bybit.merkle.generic.Self;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericMerkleTree<T extends Balance> implements Serializable {
    private Self<T> self;
    private List<Path<T>> path;

    public boolean validate() {
        if (this.self == null || CollectionUtils.isEmpty(this.path)) {
            return false;
        }
        if (!this.self.validate()) {
            return false;
        }
        if (!path.get(0).validate()) {
            return false;
        }
        if (path.get(0).getType() == null || Objects.equals(path.get(0).getType(), self.getType())) {
            return false;
        }
        String left, right;
        if (self.getType() == 1) {//左
            left = self.getHash();
            right = path.get(0).getHash();
        } else { //右
            left = path.get(0).getHash();
            right = self.getHash();
        }
        int height = 1;
        Path<T> node = new Path<>(left, right, self.getBalance(), path.get(0).getBalance(), height + 1, getTypeBySibling(path.get(1)));
        for (int i = 1; i < path.size() - 1; i++) {
            height++;
            if (!path.get(i).validate()) {
                return false;
            }
            if (!node.validate()) {
                return false;
            }
            Path<T> currentSibling = path.get(i);
            if (currentSibling.getType() == 1) {
                left = currentSibling.getHash();
                right = node.getHash();
            } else {
                left = node.getHash();
                right = currentSibling.getHash();
            }
            Integer type = getTypeBySibling(path.get(i + 1));
            node = new Path<>(left, right, node.getBalance(), path.get(i).getBalance(), height + 1, type);
        }
        Path<T> root = path.get(path.size() - 1);

        return node.getHash().equals(root.getHash())
                && node.getBalance().equals(root.getBalance())
                && Objects.equals(node.getHeight(), root.getHeight());
    }

    private Integer getTypeBySibling(Path<T> path) {
        Integer siblingType = path.getType();
        return siblingType == 3 ? 3 : siblingType == 1 ? 2 : 1;
    }
}