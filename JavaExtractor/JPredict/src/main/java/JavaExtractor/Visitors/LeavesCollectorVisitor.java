package JavaExtractor.Visitors;

import JavaExtractor.Common.Common;
import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.StopWordsFilter;
import JavaExtractor.Common.TFIDF;
import JavaExtractor.FeaturesEntities.Property;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.TreeVisitor;

import java.util.ArrayList;
import java.util.List;

public class LeavesCollectorVisitor extends TreeVisitor {
  private final ArrayList<Node> m_Leaves = new ArrayList<>();
  private final CommandLineValues m_CommandLineValues;
  private List<String> collection;

  public LeavesCollectorVisitor(CommandLineValues commandLineValues, List<String> collection) {
    this.m_CommandLineValues = commandLineValues;
    this.collection = collection;
  }

  // @Override
  // public void process(Node node) {

  //   if (m_CommandLineValues.InlineComments) {
  //     // If node has comment add it to leaves.
  //     // Check if it is not javadoc or contains code.
  //     Optional<Comment> com = node.getComment();
  //     if (com.isPresent() && !(com.get() instanceof JavadocComment) && !containsCode(com.get().getContent())) {
  //       // TODO: Check if filtering works properly
  //       String updatedComment = StopWordsFilter.removeStopWords(com.get().getContent());
  //       m_Leaves.add(node.setComment(com.get().setContent(updatedComment)));
  //     }
  //   }

  //   if (node instanceof Comment) {
  //     return;
  //   }

  //   boolean isLeaf = false;
  //   boolean isGenericParent = isGenericParent(node);
  //   if (hasNoChildren(node) && isNotComment(node)) {
  //     if (!node.toString().isEmpty() && (!"null".equals(node.toString()) || (node instanceof NullLiteralExpr))) {
  //       m_Leaves.add(node);
  //       isLeaf = true;
  //     }
  //   }

  //   int childId = getChildId(node);
  //   node.setData(Common.ChildId, childId);
  //   Property property = new Property(node, isLeaf, isGenericParent);
  //   node.setData(Common.PropertyKey, property);
  // }

  @Override
  public void process(Node node) {
    // TODO: Understand this statement
    if (node instanceof Comment) {
      return;
    }

    // check if comments have to be included
    if (m_CommandLineValues.IncludeComments) {

      // to include ophaned comments change empty list to
      // node.getAllContainedComments()
      List<Comment> comments = new ArrayList<>();

      // check if current node has associated comment
      if (node.getComment().isPresent()) {
        comments.add(node.getComment().get());
      }

      // loop through comments
      for (Comment comment : comments) {
        comment.setParentNode(node);

        // get content of comment and set it to lowercase
        String content = comment.getContent().toLowerCase();

        // check if stopwords have to be excluded
        if (m_CommandLineValues.ExcludeStopwords) {
          content = StopWordsFilter.removeStopWords(content);
        }

        // check if tfidf should be used
        if (m_CommandLineValues.IncludeTFIDF) {
          content = TFIDF.getSentence(content, collection, m_CommandLineValues.NumberKeywords);
        }

        // set new content of comments
        comment.setContent(content);

        // set properties and add to leaves
        int childId = getChildId(comment);
        comment.setData(Common.ChildId, childId);
        Property property = new Property(comment, true, false);
        comment.setData(Common.PropertyKey, property);
        m_Leaves.add(comment);
      }
    }

    boolean isLeaf = false;
    boolean isGenericParent = isGenericParent(node);
    if (hasNoChildren(node) && isNotComment(node)) {
      if (!node.toString().isEmpty() && (!"null".equals(node.toString()) || (node instanceof NullLiteralExpr))) {
        m_Leaves.add(node);
        isLeaf = true;
      }
    }

    int childId = getChildId(node);
    node.setData(Common.ChildId, childId);
    Property property = new Property(node, isLeaf, isGenericParent);
    node.setData(Common.PropertyKey, property);
  }

  private boolean containsCode(String comment) {
    return ("//" + comment).matches("^\\s*.*;\\s*$");
  }

  private boolean isGenericParent(Node node) {
    return (node instanceof ClassOrInterfaceType)
        && ((ClassOrInterfaceType) node).getTypeArguments().isPresent()
        && ((ClassOrInterfaceType) node).getTypeArguments().get().size() > 0;
  }

  private boolean hasNoChildren(Node node) {
    return node.getChildNodes().size() == 0;

  }

  private boolean isNotComment(Node node) {
    return !(node instanceof Comment) && !(node instanceof Statement);
  }

  public ArrayList<Node> getLeaves() {
    return m_Leaves;
  }

  private int getChildId(Node node) {
    Node parent = node.getParentNode().get();
    List<Node> parentsChildren = parent.getChildNodes();
    int childId = 0;
    for (Node child : parentsChildren) {
      if (child.getRange().equals(node.getRange())) {
        return childId;
      }
      childId++;
    }
    return childId;
  }
}
