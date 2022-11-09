package tutorial.mintic.security.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class Permission {
    @Id
    private String _id;
    private String url; /*/users*/
    private String method; /*POST*/

    public Permission(String url, String method) {
        this.url = url;
        this.method = method;
    }
}
