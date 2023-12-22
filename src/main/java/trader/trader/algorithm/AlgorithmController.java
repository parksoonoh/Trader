package trader.trader.algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import trader.trader.form.StickForm;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AlgorithmController {
    private ArrayList<StickForm> stickForms;

}
