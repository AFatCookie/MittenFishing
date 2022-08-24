package me.afatcookie.mittenfishing.utils;
import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

@SuppressWarnings("unused")
public class RecipeBuilder {

    private final ShapedRecipe recipe;

    /**
     * Uses the materials' name as the identifier, and the result of the recipe
     * @param material material to use.
     */
    public RecipeBuilder(Material material){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), material.toString()), new ItemCreator(material).getItemStack());
    }

    /**
     * Uses the itemStacks' string as the identifier, and the result of the recipe
     * @param itemStack itemStack to use.
     */
    public RecipeBuilder(ItemStack itemStack){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), itemStack.toString()), itemStack);
    }

    /**
     * Uses the itemStacks' string as an identifier, and the material as the result of the recipe.
     * @param itemStack itemStack to use.
     * @param material material to use.
     */
    public RecipeBuilder(Material itemStack, Material material){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), itemStack.toString()), new ItemCreator(material).getItemStack());
    }

    /**
     * Uses the materials' string value as an identifier, and the itemStack as the result of the recipe.
     * @param material material to use.
     * @param itemStack itemStack to use.
     */
    public RecipeBuilder(Material material, ItemStack itemStack){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), material.toString()), itemStack);
    }

    /**
     * Uses the identifier as the identifier for the recipe, and the material as the result of the recipe.
     * @param identifier String to be used as an identifier
     * @param material material of use.
     */
    public RecipeBuilder(String identifier, Material material){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), identifier), new ItemCreator(material).getItemStack());
    }

    /**
     * Uses the identifier as the identifier for the  recipe, and the itemStack as the result of the recipe.
     * @param identifier String to be used as an identifier
     * @param itemStack itemStack of use.
     */
    public RecipeBuilder(String identifier, ItemStack itemStack){
        this.recipe = new ShapedRecipe(new NamespacedKey(MittenFishing.getInstance(), identifier), itemStack);
    }

    /**
     * uses the NamespacedKey as the recipe identifier, and the material as the result of the recipe.
     * @param key key to be used.
     * @param material material of use.
     */
    public RecipeBuilder(NamespacedKey key, Material material){
        this.recipe = new ShapedRecipe(key, new ItemCreator(material).getItemStack());
    }

    /**
     * uses the NamespacedKey as the recipe identifier, and the itemStack as the result of the recipe.
     * @param key key to be used.
     * @param itemStack itemSTack of use.
     */
    public RecipeBuilder(NamespacedKey key, ItemStack itemStack){
        this.recipe = new ShapedRecipe(key, itemStack);
    }

    /**
     * replace this instance's recipe with the parameterized recipe.
     * @param recipe recipe to replace this instance's recipe with.
     */
    public RecipeBuilder(ShapedRecipe recipe){
        this.recipe = recipe;
    }

    /**
     * clone this instance's recipe
     * @return cloned recipe.
     */
    public RecipeBuilder reCreate(){
        return new RecipeBuilder(this.recipe);
    }

    /**
     * shape the recipe using a String list. The recipe designing works as followed:
     * Ex: " X ", " X ", " X "
     *
     * the X character is where a material would go. you can set the material using this Character. the first string is
     * the top row of the crafting grid of a crafting table, the second string is the middle row of the crafting grid
     * of a crafting table, and the third string is the bottom row of the crafting grid of a crafting table. if you don't
     * want a material at a certain part, leave it blank.
     *
     * @param shape way of shape for recipe
     * @return updated recipe of this instance's recipe
     */
    public RecipeBuilder shapeRecipe(String... shape){
        recipe.shape(shape);
        return this;
    }

    /**
     * Top line of Recipe filled. Uses Character 'T'.
     * @return updated recipe of this instance's recipe
     */
    public RecipeBuilder topLineOnlyRecipe(){
        recipe.shape("TTT", "   ", "   ");
        return this;
    }

    /**
     * Middle line of recipe filled. Uses Character 'M'.
     * @return updated recipe of this instance's recipe
     */
    public RecipeBuilder middleLineOnlyRecipe(){
        recipe.shape("   ", "MMM", "   ");
        return this;
    }

    /**
     * Bottom line of recipe filled. Uses Character 'B'.
     * @return updated recipe of this instance's recipe
     */
    public RecipeBuilder bottomLineOnlyRecipe(){
        recipe.shape("   ", "   ", "BBB");
        return this;
    }

    /**
     * Makes a ring around middle slot of crafting table grid. Uses Character 'X'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder ringRecipe(){
        recipe.shape("XXX", "X X", "XXX");
        return this;
    }

    /**
     * Left side of recipe filled. Uses Character 'L'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder leftSideRecipe(){
        recipe.shape("L  ", "L  ", "L  ");
        return this;
    }

    /**
     * Middle line going down of recipe filled. Uses Character 'M'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder middleLineRecipe(){
        recipe.shape(" M ", " M ", " M ");
        return this;
    }

    /**
     * Right side of recipe filled. Uses Character 'R'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder rightSideRecipe(){
        recipe.shape("  R", "  R", "  R");
        return this;
    }

    /**
     * Fills both sides of recipe. uses Character 'S'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder sidesRecipe(){
        recipe.shape("S S", "S S", "S S");
        return this;
    }

    /**
     * fills a diagonal line going from right at the top to left at the bottom, Uses Character 'A'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder angledRightRecipe(){
        recipe.shape("  A", " A ", "A  ");
        return this;

    }

    /**
     * Fills a diagonal line going from left at the top to right at the bottom, Uses Character 'L'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder angledLeftRecipe(){
        recipe.shape("L  ", " L ", "  L");
        return this;
    }

    /**
     * Fills a cross design for the recipe. Uses Character 'C'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder crossRecipe(){
        recipe.shape(" C ", "CCC", " C ");
        return this;
    }

    /**
     * Fills an "X" design for the recipe. uses Character 'X'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder xRecipe(){
        recipe.shape("X X", " X ", "X X");
        return this;
    }

    /**
     * Fills every other slot for the recipe. So it skips the first one, and places one in the next slot and so on. uses
     * character 'E'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder everyOtherRecipe(){
        recipe.shape(" E ", "E E", " E ");
        return this;
    }

    /**
     * Fills four corners of recipe. Uses Character 'F'.
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder fourCornersRecipe(){
        recipe.shape("F F", "   ", "F F");
        return this;
    }

    /**
     * sets the ingredient of specified character in crafting grid.
     * @param character where material will be used
     * @param material material of use
     * @return updated recipe of this instance's recipe.
     */
    public RecipeBuilder setIngredient(Character character, Material material){
        recipe.setIngredient(character, material);
        return this;
    }

    public RecipeBuilder setIngredient(Character character, ItemStack itemStack){
        recipe.setIngredient(character, new RecipeChoice.ExactChoice(itemStack));
        return this;
    }

    /**
     * Set the group that the recipe will show up in on the viewers client
     * @param group group to show up in.
     * @return updated recipe for this instance's recipe.
     */
    public RecipeBuilder setGroup(String group){
        recipe.setGroup(group);
        return this;
    }

    /**
     * build the recipe
     * @return built recipe.
     */
    public ShapedRecipe build(){
        return this.recipe;
    }

}