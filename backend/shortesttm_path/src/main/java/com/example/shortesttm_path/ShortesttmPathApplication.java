package com.example.shortesttm_path;

import com.example.shortesttm_path.util.ShortestPathUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ShortesttmPathApplication {

	public static void main(String[] args) throws IOException {
//		SpringApplication.run(ShortesttmPathApplication.class, args);
		new ShortestPathUtil().printShortestPath("Papineau", "Mont-Royal");
	}

}
