package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.*;
import entities.*;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path1"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
//		ModelData treeModelData = OBJFileLoader.loadOBJ("tree");
//		RawModel treeModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), treeModelData.getVertices(), treeModelData.getIndices());
//		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel");
//		RawModel grassModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(), grassModelData.getVertices(), grassModelData.getIndices());
//		
//		
//		TexturedModel tree = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));
//		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture2")));
//		TexturedModel flower = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("flower")));
//		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("fern")));


		RawModel model = OBJLoader.loadObjModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grassTexture2")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("flower")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("fern")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		int n = 1000;
		for(int i=0;i<n;i++){
			if(i % 7 == 0) {
				entities.add(new Entity(grass, new Vector3f(random.nextFloat() * n - 200, 0, random.nextFloat() * -400), 0, 0, 0, 1.8f));
				entities.add(new Entity(grass, new Vector3f(random.nextFloat() * n - 200, 0, random.nextFloat() * 400), 0, 0, 0, 1.8f));
				entities.add(new Entity(flower, new Vector3f(random.nextFloat() * n - 200, 0, random.nextFloat() * -400), 0, 0, 0, 2.3f));
				entities.add(new Entity(flower, new Vector3f(random.nextFloat() * n - 200, 0, random.nextFloat() * 400), 0, 0, 0, 2.3f));
			}
			if(i % 3 == 0) {				
				entities.add(new Entity(fern, new Vector3f(random.nextFloat() * n - 200,0,random.nextFloat() * -400),0, random.nextFloat() * 360,0,0.9f));
				entities.add(new Entity(fern, new Vector3f(random.nextFloat() * n - 200,0,random.nextFloat() * 400),0, random.nextFloat() * 360,0,0.9f));
				entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * n - 400,0,random.nextFloat() * -600),0,0,0, random.nextFloat() * 1 + 4));
				entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * n - 400,0,random.nextFloat() * 600),0,0,0, random.nextFloat() * 1 + 4));
			}
		}
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);
		Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap);
		Terrain terrain4 = new Terrain(0, 0, loader, texturePack, blendMap);
		Terrain terrain5 = new Terrain(1, 1, loader, texturePack, blendMap);
				
		MasterRenderer renderer = new MasterRenderer();

		ModelData playerModelData = OBJFileLoader.loadOBJ("person");
		RawModel playerModel = loader.loadToVAO(playerModelData.getVertices(), playerModelData.getTextureCoords(), playerModelData.getVertices(), playerModelData.getIndices());
		
		TexturedModel player = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player p = new Player(player, new Vector3f(100, 0, -50), 0, 0, 0, 0.5f);
		Camera camera = new Camera(p);	
		
		while(!Display.isCloseRequested()){
			camera.move();
			
			p.move();
			renderer.processEntity(p);
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);
			renderer.processTerrain(terrain4);
			renderer.processTerrain(terrain5);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
