import lombok.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingBody {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private Date checkin;
    private Date checkout;
    private String additionalneeds;


}
