package com.bybit;

import com.bybit.util.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerkleTreeVersion2 implements Serializable {


    public enum Asset {

        AGLA((short) 1, 8, RoundingMode.DOWN, "AGLA"),
        APT((short) 2, 8, RoundingMode.DOWN, "APT"),
        ATOM((short) 3, 8, RoundingMode.DOWN, "ATOM"),
        AVAX((short) 4, 8, RoundingMode.DOWN, "AVAX"),
        BIT((short) 5, 8, RoundingMode.DOWN, "BIT"),
        BTC((short) 6, 8, RoundingMode.DOWN, "BTC"),
        BUSD((short) 7, 8, RoundingMode.DOWN, "BUSD"),
        DAI((short) 8, 8, RoundingMode.DOWN, "DAI"),
        DOGE((short) 9, 8, RoundingMode.DOWN, "DOGE"),
        DOT((short) 10, 8, RoundingMode.DOWN, "DOT"),
        DYDX((short) 11, 8, RoundingMode.DOWN, "DYDX"),
        EOS((short) 12, 8, RoundingMode.DOWN, "EOS"),
        ETH((short) 13, 8, RoundingMode.DOWN, "ETH"),
        FTM((short) 14, 8, RoundingMode.DOWN, "FTM"),
        LTC((short) 15, 8, RoundingMode.DOWN, "LTC"),
        MATIC((short) 16, 8, RoundingMode.DOWN, "MATIC"),
        SOL((short) 17, 8, RoundingMode.DOWN, "SOL"),
        USDC((short) 18, 8, RoundingMode.DOWN, "USDC"),
        USDT((short) 19, 8, RoundingMode.DOWN, "USDT"),
        XRP((short) 20, 8, RoundingMode.DOWN, "XRP");

        private final short code;
        private final int scale;

        private final RoundingMode roundingMode;
        private final String jsonPropertyName;

        Asset(short code, int scale, RoundingMode roundingMode, String jsonPropertyName) {
            this.code = code;
            this.scale = scale;
            this.roundingMode = roundingMode;
            this.jsonPropertyName = jsonPropertyName;
        }

        public short getCode() {
            return code;
        }

        public int getScale() {
            return scale;
        }

        public RoundingMode getRoundingMode() {
            return roundingMode;
        }

        public String getJsonPropertyName() {
            return jsonPropertyName;
        }
    }


    private Self self;
    private List<Path> path;

    private List<Path> showPath = new ArrayList<>();

    public static class BalanceDeserializer extends JsonDeserializer<Balance> {
        @Override
        public Balance deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            Balance balance = new Balance();
            EnumMap<Asset, String> balancesMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                String assetName = parser.getCurrentName();
                Asset asset = Asset.valueOf(assetName);

                parser.nextToken();
                String value = parser.getText();

                balancesMap.put(asset, value);

                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
    @Data
    @JsonDeserialize(using = BalanceDeserializer.class)
    public static class Balance {
        private EnumMap<Asset, String> balances;

        public Balance() {
            balances = new EnumMap<>(Asset.class);
            for (Asset asset : Asset.values()) {
                balances.put(asset, "0");
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Balance balance = (Balance) o;
            return Objects.equals(balances, balance.balances);
        }

        @Override
        public int hashCode() {
            return Objects.hash(balances);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            for (Asset asset : Asset.values()) {
                sb.append("\"")
                        .append(asset.name())
                        .append("\":\"")
                        .append(balances.get(asset))
                        .append("\", ");
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and space
            sb.append('}');
            return sb.toString();
        }

        public Balance add(Balance balance) {
            Balance res = new Balance();
            for (Asset asset : Asset.values()) {
                BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
                BigDecimal otherValue = new BigDecimal(balance.balances.get(asset));
                BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getRoundingMode());
                res.balances.put(asset, sum.toPlainString());
            }
            return res;
        }

        public boolean validate() {
            try {
                for (Asset asset : Asset.values()) {
                    BigDecimal value = new BigDecimal(this.balances.get(asset));
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Path{
        private String auditId;
        private Integer height;
        private Integer type;
        private String hash;
        private Balance balance;
        private Integer position;
        private String nonce;

        @Override
        public String toString() {
            return "{" +
                    "\"height\":" + height +
                    ", \"type\":" + type +
                    ", \"hash\":\"" + hash + "\"" +
                    ", \"balance\":" + balance +
                    '}';
        }

        public Path(int height, Integer type, String hash, Balance balance){
            this.setHeight(height);
            this.setType(type);
            this.setHash(hash);
            this.setBalance(balance);
        }

        public Path(Self self){
            this.auditId = self.getAuditId();
            this.height = self.getHeight();
            this.type = self.getType();
            this.hash = self.getHash();
            this.balance = self.getBalance();
            this.position = self.getPosition();
            this.nonce = self.getNonce();
        }

        /**
         *
         * @param leftHash
         * @param rightHash
         * @param balance1
         * @param balance2
         * @param height
         * @param type
         * @return
         */
        public static Path instance(String leftHash, String rightHash, Balance balance1, Balance balance2, int height, Integer type) {
            if (!balance1.validate() || !balance2.validate()) {
                return new Path();
            }
            Balance balance = balance1.add(balance2);
            String balanceConcatenation = balance.balances.entrySet().stream()
                    .map(entry -> entry.getValue())
                    .collect(Collectors.joining());
            String data = leftHash + rightHash + balanceConcatenation + height;
            String s = CryptoUtil.sha256Str(data);
            Path path = new Path();
            path.setHeight(height);
            path.setType(type);
            path.setHash(s);
            path.setBalance(balance);
            return path;
        }

        public boolean validate(){
            if(this.balance == null || !this.balance.validate() || StringUtils.isEmpty(hash) || this.type < 1 || this.type > 4) {
                return false;
            }
            return this.height >= 1;
        }

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Self {
        private String auditId;

        private String userHash;
        private String nonce;
        private String hash;
        private Balance balance;
        private Integer type;
        private Integer height;
        private Integer position;

        @Override
        public String toString() {
            return "{" +
                    "\"nonce\":\"" + nonce + "\"" +
                    ", \"hash\":\"" + hash + "\"" +
                    ", \"balance\":" + balance +
                    ", \"type\":" + type +
                    ", \"height\":" + height +
                    '}';
        }

        /**
         * @return
         */
        public boolean validate() {
            String balanceConcatenation = balance.getBalances().entrySet().stream()
                    .map(entry -> entry.getValue())
                    .collect(Collectors.joining());
            String data = userHash + balanceConcatenation;
            String s = CryptoUtil.sha256Str(data);
            boolean validate = StringUtils.equals(s, hash);
            return validate;
        }

        public Path toPath() {
            Path path = new Path();
            path.setBalance(this.getBalance());
            path.setHash(this.getHash());
            path.setHeight(this.getHeight());
            path.setType(this.getType());
            return path;
        }
    }

    public boolean quickValidate(){
        return false;
    }

    public MerkleTreeVersion2 buildSHowMerkleTree(){
        this.path = this.showPath;
        return this;
    }

    public boolean validate(){
        if(this.self == null || CollectionUtils.isEmpty(this.path)) {
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
        if(self.getType() == 1){//左
            left = self.getHash();
            right = path.get(0).getHash();
        } else { //右
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

            //把对应要展示的节点保留下来
            if(i == path.size() - 3) {
                Path secondLast = path.get(path.size() - 2);
                Path last = path.get(path.size() - 1);
                showPath.add(last);
                if(type == 1) {
                    showPath.add(node);
                    showPath.add(secondLast);
                } else if(type == 2) {
                    showPath.add(secondLast);
                    showPath.add(node);
                }
                if(self.getType() == 1) {
                    showPath.add(new Path(self));
                    showPath.add(path.get(0));
                } else {
                    showPath.add(path.get(0));
                    showPath.add(new Path(self));
                }
            }
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
