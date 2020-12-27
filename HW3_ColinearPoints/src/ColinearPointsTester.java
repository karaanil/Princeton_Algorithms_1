
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import edu.princeton.cs.algs4.In;

public class ColinearPointsTester {
    
    public static void main(String[] args) {
        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver(ColinearPointsTester.class.getClassLoader());
        
        try {
            Resource[] resources = resolver.getResources("testInputs/*.txt");
            for (Resource item : resources) {
                FastCollinearPoints.main(new String[] { item.getURL().toString() });
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
