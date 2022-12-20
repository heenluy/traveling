package dev.henriqueluiz.travelling.model;

import static jakarta.persistence.GenerationType.SEQUENCE;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    
    @Id
    @GeneratedValue(
        strategy = SEQUENCE,
        generator = "token_seq"
    )
    @SequenceGenerator(
        name = "token_seq",
        sequenceName = "token_seq_id",
        initialValue = 1
    )
    private Long tokenId;

    @JsonProperty("refresh_token")
    private String token;
    private Instant validUtil = Instant.now().plus(40, ChronoUnit.SECONDS);
    private Boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    
    public Long getTokenId() {
        return tokenId;
    }
    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Instant getValidUtil() {
        return validUtil;
    }
    public void setValidUtil(Instant validUtil) {
        this.validUtil = validUtil;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RefreshToken other = (RefreshToken) obj;
        if (tokenId == null) {
            if (other.tokenId != null)
                return false;
        } else if (!tokenId.equals(other.tokenId))
            return false;
        return true;
    }
}
