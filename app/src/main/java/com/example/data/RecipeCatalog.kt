package com.example.data

import com.example.R

object RecipeCatalog {

    val pantryStaples: List<PantryStaple> = listOf(
        // Mini-Fridge
        PantryStaple("eggs", "Eggs", StapleGroup.FRIDGE, "~$0.30/egg"),
        PantryStaple("greek_yogurt", "Greek Yogurt", StapleGroup.FRIDGE, "~$0.90/cup"),
        PantryStaple("cheese", "Shredded Cheese", StapleGroup.FRIDGE, "~$0.45/serv"),
        PantryStaple("cottage_cheese", "Cottage Cheese", StapleGroup.FRIDGE, "~$0.85/cup"),
        PantryStaple("hummus", "Hummus", StapleGroup.FRIDGE, "~$0.65/serv"),
        PantryStaple("milk", "Milk / Oat Milk", StapleGroup.FRIDGE, "~$0.30/cup"),
        PantryStaple("tofu", "Firm Tofu", StapleGroup.FRIDGE, "~$1.10/half"),
        PantryStaple("salsa", "Salsa / Hot Sauce", StapleGroup.FRIDGE, "~$0.20/serv"),

        // Dry Pantry
        PantryStaple("oats", "Rolled Oats", StapleGroup.PANTRY, "~$0.25/cup"),
        PantryStaple("peanut_butter", "Peanut Butter", StapleGroup.PANTRY, "~$0.25/tbsp"),
        PantryStaple("black_beans", "Canned Black Beans", StapleGroup.PANTRY, "~$0.55/serv"),
        PantryStaple("chickpeas", "Canned Chickpeas", StapleGroup.PANTRY, "~$0.60/serv"),
        PantryStaple("white_beans", "Cannellini Beans", StapleGroup.PANTRY, "~$0.60/serv"),
        PantryStaple("rice_cup", "90-Sec Rice Cup", StapleGroup.PANTRY, "~$0.85/cup"),
        PantryStaple("ramen", "Ramen Noodles", StapleGroup.PANTRY, "~$0.40/pack"),
        PantryStaple("tuna", "Canned Tuna", StapleGroup.PANTRY, "~$1.15/can"),
        PantryStaple("tortilla", "Tortillas / Pita", StapleGroup.PANTRY, "~$0.45/wrap"),
        PantryStaple("bread", "Whole-Grain Bread", StapleGroup.PANTRY, "~$0.35/slice"),

        // Quick Fresh
        PantryStaple("spinach", "Baby Spinach", StapleGroup.PRODUCE, "~$0.45/handful"),
        PantryStaple("banana", "Banana", StapleGroup.PRODUCE, "~$0.25/each"),
        PantryStaple("berries", "Frozen / Fresh Berries", StapleGroup.PRODUCE, "~$0.65/cup"),
        PantryStaple("avocado", "Avocado", StapleGroup.PRODUCE, "~$0.75/half"),
        PantryStaple("tomato", "Cherry Tomatoes", StapleGroup.PRODUCE, "~$0.45/handful"),
        PantryStaple("cucumber", "Cucumber", StapleGroup.PRODUCE, "~$0.40/half")
    )

    val recipes: List<Recipe> = listOf(
        Recipe(
            id = "crispy_egg_bean_bowl",
            title = "Microwave Egg & Black Bean Rice Bowl",
            tagline = "High-fiber Tex-Mex comfort bowl faster than delivery.",
            prepMinutes = 6,
            costPerServing = 1.85,
            calories = 460,
            proteinGrams = 22,
            fiberGrams = 10,
            equipment = EquipmentType.MICROWAVE,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_protein_bowl,
            ingredients = listOf(
                IngredientItem("eb_rice", "90-second brown rice cup", 1.0, "cup", 0.85, "rice_cup"),
                IngredientItem("eb_beans", "Canned black beans (rinsed)", 0.5, "cup", 0.50, "black_beans"),
                IngredientItem("eb_egg", "Large eggs", 2.0, "", 0.55, "eggs"),
                IngredientItem("eb_avocado", "Ripe avocado, sliced", 0.25, "", 0.40, "avocado"),
                IngredientItem("eb_salsa", "Salsa or hot sauce", 2.0, "tbsp", 0.15, "salsa")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Heat rice & beans",
                    "Vent the rice cup and microwave for 90 seconds. Pour into a deep bowl, stir in drained black beans, and microwave 45 seconds more until steaming.",
                    durationSeconds = 135
                ),
                CookingStep(
                    2,
                    "Mug-poach the eggs",
                    "Crack eggs into a greased microwave-safe mug with 1 tbsp water. Pierce yolks gently with a fork, cover with a paper towel, and microwave in 30-second bursts.",
                    durationSeconds = 75
                ),
                CookingStep(
                    3,
                    "Top & finish",
                    "Slide the warm eggs onto the rice and beans. Top with sliced avocado, salsa, and a pinch of salt or chili flakes.",
                    durationSeconds = 45
                )
            ),
            dormHack = "No knife for avocado? Scoop directly from the half with a spoon and mash straight onto the warm beans."
        ),
        Recipe(
            id = "pb_berry_oats",
            title = "Peanut Butter Berry Power Oats",
            tagline = "Creamy grab-and-go jar packed with slow-release fuel.",
            prepMinutes = 4,
            costPerServing = 1.45,
            calories = 410,
            proteinGrams = 18,
            fiberGrams = 9,
            equipment = EquipmentType.NO_COOK,
            isVegetarian = true,
            isBreakfast = true,
            isLateNight = false,
            imageResId = R.drawable.img_meal_oats_yogurt,
            ingredients = listOf(
                IngredientItem("po_oats", "Rolled or quick oats", 0.5, "cup", 0.25, "oats"),
                IngredientItem("po_yogurt", "Plain or vanilla Greek yogurt", 0.5, "cup", 0.65, "greek_yogurt"),
                IngredientItem("po_milk", "Milk or oat milk", 0.5, "cup", 0.20, "milk"),
                IngredientItem("po_pb", "Creamy peanut butter", 1.5, "tbsp", 0.25, "peanut_butter"),
                IngredientItem("po_berries", "Frozen or fresh berries", 0.5, "cup", 0.50, "berries")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Whisk base in a jar",
                    "Stir oats, Greek yogurt, milk, and a pinch of cinnamon in a mason jar or cereal bowl until smooth.",
                    durationSeconds = 60
                ),
                CookingStep(
                    2,
                    "Swirl peanut butter & berries",
                    "Spoon peanut butter over top (heat 15 sec if stiff) and pile on berries. Eat right away with quick oats, or chill overnight for thicker texture.",
                    durationSeconds = 60
                )
            ),
            dormHack = "Buy frozen mixed berries instead of fresh—they cost half as much, never mold in a mini-fridge, and chill the oats naturally."
        ),
        Recipe(
            id = "sesame_garlic_ramen",
            title = "10-Min Sesame Peanut Ramen Upgrade",
            tagline = "Turns a 40¢ noodle pack into a balanced, protein-rich meal.",
            prepMinutes = 8,
            costPerServing = 2.05,
            calories = 490,
            proteinGrams = 21,
            fiberGrams = 5,
            equipment = EquipmentType.ONE_PAN,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_noodles_stirfry,
            ingredients = listOf(
                IngredientItem("sr_ramen", "Instant ramen noodles (discard packet)", 1.0, "pack", 0.40, "ramen"),
                IngredientItem("sr_egg", "Large egg", 1.0, "", 0.30, "eggs"),
                IngredientItem("sr_pb", "Peanut butter", 1.5, "tbsp", 0.25, "peanut_butter"),
                IngredientItem("sr_spinach", "Baby spinach", 1.5, "cups", 0.45, "spinach"),
                IngredientItem("sr_soy", "Soy sauce + garlic powder + sriracha", 1.0, "tbsp", 0.20, "salsa")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Boil noodles & soft egg",
                    "Bring water to a boil (or pour boiling electric-kettle water into a pot). Cook noodles 3 minutes; gently lower in an egg during the final 6 minutes if boiling separately, or whisk egg in at the end.",
                    durationSeconds = 180
                ),
                CookingStep(
                    2,
                    "Mix savory peanut sauce",
                    "In your serving bowl, whisk peanut butter, soy sauce, garlic powder, sriracha, and 2 tbsp hot noodle water until silky.",
                    durationSeconds = 60
                ),
                CookingStep(
                    3,
                    "Wilt spinach & toss",
                    "Drop spinach into the hot noodles for 20 seconds to wilt, drain most water, and toss everything into the peanut sauce bowl.",
                    durationSeconds = 60
                )
            ),
            dormHack = "No stove? Pour boiling electric-kettle water over noodles & spinach in a covered ceramic bowl for 4 minutes, then stir in the peanut sauce."
        ),
        Recipe(
            id = "med_chickpea_wrap",
            title = "Mediterranean Chickpea Hummus Wrap",
            tagline = "Crunchy, bright, and zero cooking required between classes.",
            prepMinutes = 5,
            costPerServing = 2.25,
            calories = 430,
            proteinGrams = 19,
            fiberGrams = 11,
            equipment = EquipmentType.NO_COOK,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = false,
            imageResId = R.drawable.img_meal_wrap_toast,
            ingredients = listOf(
                IngredientItem("mc_pita", "Large whole-wheat pita or tortilla", 1.0, "", 0.50, "tortilla"),
                IngredientItem("mc_chickpeas", "Canned chickpeas (drained)", 0.5, "cup", 0.60, "chickpeas"),
                IngredientItem("mc_hummus", "Roasted garlic hummus", 3.0, "tbsp", 0.65, "hummus"),
                IngredientItem("mc_spinach", "Baby spinach", 1.0, "cup", 0.30, "spinach"),
                IngredientItem("mc_cucumber", "Diced cucumber & cherry tomatoes", 0.5, "cup", 0.45, "cucumber")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Smash the chickpeas",
                    "In a small bowl, lightly mash drained chickpeas with a fork, a pinch of salt, pepper, and lemon juice or cumin.",
                    durationSeconds = 90
                ),
                CookingStep(
                    2,
                    "Layer & roll tightly",
                    "Spread hummus across the pita or tortilla, pile on spinach, smashed chickpeas, cucumber, and tomatoes. Fold ends in and roll tight.",
                    durationSeconds = 90
                )
            ),
            dormHack = "Save the other half of the canned chickpeas in a lidded container with cold water—they stay crisp in the mini-fridge for 4 days."
        ),
        Recipe(
            id = "mug_spinach_scramble",
            title = "2-Minute Mug Spinach & Cheddar Scramble",
            tagline = "Fluffy hot protein breakfast before an 8 AM lecture.",
            prepMinutes = 4,
            costPerServing = 1.50,
            calories = 320,
            proteinGrams = 21,
            fiberGrams = 2,
            equipment = EquipmentType.MICROWAVE,
            isVegetarian = true,
            isBreakfast = true,
            isLateNight = true,
            imageResId = R.drawable.img_meal_protein_bowl,
            ingredients = listOf(
                IngredientItem("ms_eggs", "Large eggs", 2.0, "", 0.55, "eggs"),
                IngredientItem("ms_milk", "Splash of milk", 2.0, "tbsp", 0.10, "milk"),
                IngredientItem("ms_spinach", "Chopped baby spinach", 0.5, "cup", 0.30, "spinach"),
                IngredientItem("ms_cheese", "Shredded cheddar cheese", 0.25, "cup", 0.40, "cheese"),
                IngredientItem("ms_toast", "Whole-grain toast (optional)", 1.0, "slice", 0.35, "bread")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Whisk in a large mug",
                    "Lightly grease a wide ceramic mug. Whisk eggs, milk, salt, and pepper vigorously with a fork for 20 seconds.",
                    durationSeconds = 30
                ),
                CookingStep(
                    2,
                    "Microwave in two bursts",
                    "Fold in spinach and cheese. Microwave 45 seconds, stir gently from the edges, then microwave 30–45 seconds until just set.",
                    durationSeconds = 90
                )
            ),
            dormHack = "Use a wide soup mug instead of a tall coffee mug—it heats evenly without rubbery edges and washes clean in seconds."
        ),
        Recipe(
            id = "spicy_tuna_avocado_bowl",
            title = "Spicy Tuna & Avocado Crunch Bowl",
            tagline = "Sushi-bowl vibes for under $3 with 31g of clean protein.",
            prepMinutes = 6,
            costPerServing = 2.65,
            calories = 450,
            proteinGrams = 31,
            fiberGrams = 7,
            equipment = EquipmentType.NO_COOK,
            isVegetarian = false,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_protein_bowl,
            ingredients = listOf(
                IngredientItem("st_rice", "90-second rice cup", 1.0, "cup", 0.85, "rice_cup"),
                IngredientItem("st_tuna", "Canned tuna in water (drained)", 1.0, "can", 1.15, "tuna"),
                IngredientItem("st_avocado", "Diced avocado", 0.5, "", 0.65, "avocado"),
                IngredientItem("st_cucumber", "Sliced cucumber", 0.5, "cup", 0.30, "cucumber"),
                IngredientItem("st_sauce", "Greek yogurt or mayo + sriracha", 1.5, "tbsp", 0.20, "greek_yogurt")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Mix spicy tuna",
                    "Drain tuna well and toss with Greek yogurt (or mayo), sriracha, and a splash of soy sauce until creamy.",
                    durationSeconds = 60
                ),
                CookingStep(
                    2,
                    "Warm rice & assemble",
                    "Microwave rice cup 90 seconds (or use chilled leftover rice). Top with spicy tuna, cucumber, and avocado.",
                    durationSeconds = 120
                )
            ),
            dormHack = "Swapping mayo for plain Greek yogurt boosts protein by 4g and cuts cost if you already keep yogurt for breakfast."
        ),
        Recipe(
            id = "lemon_white_bean_toast",
            title = "Lemon Garlic White Bean Smash Toast",
            tagline = "Cafe-style smashed beans on crisp toast—fancy on a dorm budget.",
            prepMinutes = 7,
            costPerServing = 1.70,
            calories = 380,
            proteinGrams = 16,
            fiberGrams = 10,
            equipment = EquipmentType.ONE_PAN,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = false,
            imageResId = R.drawable.img_meal_wrap_toast,
            ingredients = listOf(
                IngredientItem("wb_bread", "Thick whole-grain bread", 2.0, "slices", 0.65, "bread"),
                IngredientItem("wb_beans", "Cannellini white beans (rinsed)", 0.5, "cup", 0.60, "white_beans"),
                IngredientItem("wb_tomato", "Halved cherry tomatoes", 0.5, "cup", 0.45, "tomato"),
                IngredientItem("wb_spinach", "Handful of baby spinach", 0.5, "cup", 0.25, "spinach")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Toast bread crisp",
                    "Toast bread slices in a dry skillet or toaster until golden and sturdy enough for toppings.",
                    durationSeconds = 120
                ),
                CookingStep(
                    2,
                    "Warm & smash beans",
                    "Warm beans with 1 tsp olive oil, garlic powder, salt, and pepper for 2 minutes. Mash half with a fork and add a squeeze of lemon.",
                    durationSeconds = 120
                ),
                CookingStep(
                    3,
                    "Pile on toppings",
                    "Spread garlicky white beans over toast and top with fresh spinach and cherry tomatoes.",
                    durationSeconds = 45
                )
            ),
            dormHack = "Keep lemon juice packets from campus dining halls—a single squeeze wakes up canned beans immediately."
        ),
        Recipe(
            id = "banana_cocoa_yogurt_cup",
            title = "Banana Cocoa Protein Yogurt Cup",
            tagline = "Tastes like chocolate pudding, packs 24g of protein in 3 minutes.",
            prepMinutes = 3,
            costPerServing = 1.35,
            calories = 340,
            proteinGrams = 24,
            fiberGrams = 5,
            equipment = EquipmentType.NO_COOK,
            isVegetarian = true,
            isBreakfast = true,
            isLateNight = true,
            imageResId = R.drawable.img_meal_oats_yogurt,
            ingredients = listOf(
                IngredientItem("bc_yogurt", "Plain or Greek yogurt", 1.0, "cup", 0.90, "greek_yogurt"),
                IngredientItem("bc_banana", "Ripe sliced banana", 1.0, "", 0.25, "banana"),
                IngredientItem("bc_pb", "Peanut butter", 1.0, "tbsp", 0.20, "peanut_butter"),
                IngredientItem("bc_oats", "Rolled oats for crunch", 2.0, "tbsp", 0.10, "oats")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Whip cocoa yogurt",
                    "Stir Greek yogurt with 1 tbsp unsweetened cocoa powder and a drizzle of honey or maple syrup until glossy.",
                    durationSeconds = 60
                ),
                CookingStep(
                    2,
                    "Layer banana & crunch",
                    "Top with sliced banana, peanut butter drizzle, and dry rolled oats for instant granola crunch.",
                    durationSeconds = 45
                )
            ),
            dormHack = "Toasted or dry oats give all the crunch of pricey store-bought granola for pennies per scoop."
        ),
        Recipe(
            id = "cheesy_black_bean_quesadilla",
            title = "Crispy Black Bean & Spinach Quesadilla",
            tagline = "Golden folded tortilla stuffed with fiber, greens, and melted cheese.",
            prepMinutes = 8,
            costPerServing = 1.95,
            calories = 440,
            proteinGrams = 20,
            fiberGrams = 9,
            equipment = EquipmentType.ONE_PAN,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_wrap_toast,
            ingredients = listOf(
                IngredientItem("cq_tortilla", "Large flour or corn tortilla", 1.0, "", 0.45, "tortilla"),
                IngredientItem("cq_beans", "Canned black beans (drained)", 0.5, "cup", 0.55, "black_beans"),
                IngredientItem("cq_cheese", "Shredded Monterey Jack or cheddar", 0.33, "cup", 0.50, "cheese"),
                IngredientItem("cq_spinach", "Baby spinach", 0.75, "cup", 0.30, "spinach"),
                IngredientItem("cq_salsa", "Salsa for dipping", 2.0, "tbsp", 0.15, "salsa")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Fill one half",
                    "Lay tortilla flat in a warm nonstick skillet. Layer half with cheese, black beans, spinach, and a little extra cheese to seal.",
                    durationSeconds = 60
                ),
                CookingStep(
                    2,
                    "Crisp both sides",
                    "Fold tortilla over and cook 2 minutes per side on medium heat until golden brown and bubbly inside.",
                    durationSeconds = 240
                )
            ),
            dormHack = "Microwave version: fold tortilla on a plate and heat 45 seconds under a damp paper towel—soft, melty, and zero dishes."
        ),
        Recipe(
            id = "miso_tofu_edamame_soup",
            title = "6-Min Miso Tofu & Spinach Mug Soup",
            tagline = "Warm, gut-friendly broth that rescues late-night study sessions.",
            prepMinutes = 6,
            costPerServing = 2.20,
            calories = 310,
            proteinGrams = 22,
            fiberGrams = 5,
            equipment = EquipmentType.MICROWAVE,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_noodles_stirfry,
            ingredients = listOf(
                IngredientItem("mt_tofu", "Cubed firm or silken tofu", 0.75, "cup", 1.10, "tofu"),
                IngredientItem("mt_spinach", "Baby spinach", 1.0, "cup", 0.35, "spinach"),
                IngredientItem("mt_ramen", "Half pack ramen noodles (optional)", 0.5, "pack", 0.25, "ramen"),
                IngredientItem("mt_miso", "Miso paste + soy sauce", 1.5, "tbsp", 0.40, "salsa")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Heat broth & tofu",
                    "Place cubed tofu, noodles, and 1.5 cups water in a deep microwave-safe bowl. Microwave 3 minutes until piping hot.",
                    durationSeconds = 180
                ),
                CookingStep(
                    2,
                    "Dissolve miso off-heat",
                    "Stir in miso paste and baby spinach after microwaving so probiotics stay active and greens stay bright.",
                    durationSeconds = 60
                )
            ),
            dormHack = "Miso paste keeps in the fridge for up to a year—one small tub makes 20+ bowls of instant soup."
        ),
        Recipe(
            id = "cottage_cheese_caprese_flatbread",
            title = "Whipped Cottage Cheese & Tomato Flatbread",
            tagline = "High-protein crispy pita pizza ready faster than frozen pizza.",
            prepMinutes = 7,
            costPerServing = 2.40,
            calories = 390,
            proteinGrams = 25,
            fiberGrams = 5,
            equipment = EquipmentType.ONE_PAN,
            isVegetarian = true,
            isBreakfast = false,
            isLateNight = true,
            imageResId = R.drawable.img_meal_wrap_toast,
            ingredients = listOf(
                IngredientItem("cf_pita", "Whole-wheat pita bread", 1.0, "", 0.55, "tortilla"),
                IngredientItem("cf_cottage", "Cottage cheese", 0.5, "cup", 0.85, "cottage_cheese"),
                IngredientItem("cf_tomato", "Sliced cherry tomatoes", 0.5, "cup", 0.45, "tomato"),
                IngredientItem("cf_spinach", "Baby spinach", 0.5, "cup", 0.30, "spinach"),
                IngredientItem("cf_cheese", "Sprinkle of mozzarella", 2.0, "tbsp", 0.25, "cheese")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Crisp the pita base",
                    "Toast pita in a covered skillet or toaster oven for 2 minutes until lightly crisp on the underside.",
                    durationSeconds = 120
                ),
                CookingStep(
                    2,
                    "Top & warm",
                    "Spread cottage cheese, cherry tomatoes, spinach, and mozzarella on top. Cover pan 2 minutes to melt cheese slightly, then finish with cracked pepper.",
                    durationSeconds = 120
                )
            ),
            dormHack = "No toaster oven? Pop the pita in a dry skillet with a plate on top as a lid—it traps heat and melts cheese fast."
        ),
        Recipe(
            id = "peanut_banana_oat_pancakes",
            title = "5-Min Banana Oat Skillet Cakes",
            tagline = "3-ingredient flourless pancakes naturally sweet and filling.",
            prepMinutes = 9,
            costPerServing = 1.60,
            calories = 395,
            proteinGrams = 17,
            fiberGrams = 6,
            equipment = EquipmentType.ONE_PAN,
            isVegetarian = true,
            isBreakfast = true,
            isLateNight = false,
            imageResId = R.drawable.img_meal_oats_yogurt,
            ingredients = listOf(
                IngredientItem("bp_banana", "Very ripe banana", 1.0, "", 0.25, "banana"),
                IngredientItem("bp_eggs", "Large eggs", 2.0, "", 0.55, "eggs"),
                IngredientItem("bp_oats", "Rolled or quick oats", 0.5, "cup", 0.25, "oats"),
                IngredientItem("bp_pb", "Peanut butter topping", 1.0, "tbsp", 0.25, "peanut_butter"),
                IngredientItem("bp_berries", "Berries for topping", 0.33, "cup", 0.35, "berries")
            ),
            steps = listOf(
                CookingStep(
                    1,
                    "Mash & whisk batter",
                    "Mash banana smooth with a fork in a bowl. Whisk in eggs, oats, and a pinch of cinnamon until combined.",
                    durationSeconds = 90
                ),
                CookingStep(
                    2,
                    "Cook silver-dollar cakes",
                    "Drop small 2-tbsp scoops onto a lightly oiled medium skillet. Cook 2 minutes per side until golden and easy to flip.",
                    durationSeconds = 240
                ),
                CookingStep(
                    3,
                    "Stack & drizzle",
                    "Plate warm pancakes and top with peanut butter and berries.",
                    durationSeconds = 30
                )
            ),
            dormHack = "Keep pancakes small (about 3 inches wide)—oat batter flips effortlessly without tearing."
        )
    )
}
