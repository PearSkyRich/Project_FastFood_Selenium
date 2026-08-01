import os
import re
import pandas as pd


# thư mục test java
TEST_PATH = "./src/test/java"


unit_count = 1
integration_count = 1


test_cases = []


for root, dirs, files in os.walk(TEST_PATH):

    for file in files:

        if not file.endswith(".java"):
            continue


        file_path = os.path.join(root, file)


        # đọc file java
        with open(
            file_path,
            "r",
            encoding="utf-8"
        ) as f:

            content = f.read()


        # xác định loại test
        if "\\integration\\" in file_path or "/integration/" in file_path:

            test_type = "Integration"

            prefix = "IT"


        else:

            test_type = "Unit"

            prefix = "UT"



        # lấy tên class
        class_match = re.search(
            r"class\s+(\w+)",
            content
        )


        class_name = (
            class_match.group(1)
            if class_match
            else file
        )


        # tìm các hàm test
        methods = re.findall(
            r'@Test[\s\S]*?(?:public\s+)?void\s+(\w+)\s*\(',
            content
        )


        for method in methods:


            if prefix == "UT":

                test_id = (
                    f"UT_{str(unit_count).zfill(3)}"
                )

                unit_count += 1


            else:

                test_id = (
                    f"IT_{str(integration_count).zfill(3)}"
                )

                integration_count += 1



            test_cases.append({

                "Test Case ID": test_id,

                "Type": test_type,

                "Class": class_name,

                "Test Method": method,

                "File": file,

                "Expected Result": "",

                "Actual Result": "",

                "Status": ""

            })



# tạo excel

df = pd.DataFrame(test_cases)


df.to_excel(
    "FastFood_Test_Case.xlsx",
    index=False
)


print(
    "Đã tạo FastFood_Test_Case.xlsx"
)

print(
    f"Tổng test case: {len(test_cases)}"
)